/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.cas.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.services.RegisteredService;
import org.jasig.cas.services.ServicesManager;
import org.jasig.cas.util.CasUtility;
import org.jasig.cas.util.Constants;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to delete ticket granting ticket cookie in order to log out of
 * single sign on. This controller implements the idea of the ESUP Portail's
 * Logout patch to allow for redirecting to a url on logout. It also exposes a
 * log out link to the view via the WebConstants.LOGOUT constant.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
@SuppressWarnings("deprecation")
public final class LogoutController extends AbstractController {

    /** The CORE to which we delegate for all CAS functionality. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** CookieGenerator for TGT Cookie */
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    /** CookieGenerator for Warn Cookie */
    @NotNull
    private CookieRetrievingCookieGenerator warnCookieGenerator;

    /** Logout view name. */
    @NotNull
    private String logoutView;

    @NotNull
    private ServicesManager servicesManager;
    
    @NotNull
    private DataSource dataSource;
    
	private SimpleJdbcTemplate jdbcTemplate;
    
    /**
     * Boolean to determine if we will redirect to any url provided in the
     * service request parameter.
     */
    private boolean followServiceRedirects;
    
    public LogoutController() {
        setCacheSeconds(0);
    }

	protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response)throws Exception {
		
        final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
        final String service = request.getParameter("service");
        
        if (ticketGrantingTicketId != null) {
        	
        	// 根据TGT获得登录用户名 Begin
        	String username = null;
        	if(service != null && service.length() != 0){
	        	String casServerUrl = CasUtility.getCasServerUrl(request);
	    	    Cas20ProxyTicketValidator sv = new Cas20ProxyTicketValidator(casServerUrl);
	    	    sv.setAcceptAnyProxy(true);
	        	String server = CasUtility.getCasServerRestUrl(request);
	        	String serviceTicket = getServiceTicket(server, ticketGrantingTicketId, service);
		        Assertion a = sv.validate(serviceTicket, service);
		        username = a.getPrincipal().getName();
        	}
        	// 根据TGT获得登录用户名 End
        	
            this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);
            this.ticketGrantingTicketCookieGenerator.removeCookie(response);
            this.warnCookieGenerator.removeCookie(response);
            
            try {
    			getJdbcTemplate().update(Constants.LOG_INSERT_SQL, new Object[]{username==null?"--":username, "2", "用户退出", "1", (username==null?"用户":username) + "退出", request.getRemoteAddr()});
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            
        }

        if (this.followServiceRedirects && service != null) {
            final RegisteredService rService = this.servicesManager.findServiceBy(new SimpleWebApplicationServiceImpl(service));

            if (rService != null && rService.isEnabled()) {
                return new ModelAndView(new RedirectView(service));
            }
        }

        return new ModelAndView(this.logoutView);
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setWarnCookieGenerator(final CookieRetrievingCookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    /**
     * @param centralAuthenticationService The centralAuthenticationService to
     * set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setFollowServiceRedirects(final boolean followServiceRedirects) {
        this.followServiceRedirects = followServiceRedirects;
    }

    public void setLogoutView(final String logoutView) {
        this.logoutView = logoutView;
    }

    public void setServicesManager(final ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }
    
	public final void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}
	
	protected final DataSource getDataSource() {
		return this.dataSource;
	}
	
	protected final SimpleJdbcTemplate getJdbcTemplate() {
		return this.jdbcTemplate;
	}
	
	private static String getServiceTicket(final String server,final String ticketGrantingTicket, final String service) {
		
		if (ticketGrantingTicket == null)
			return null;
		final HttpClient client = new HttpClient();
		final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);
		post.setRequestBody(new NameValuePair[] { new NameValuePair("service", service) });
		try {
			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();

			switch (post.getStatusCode()) {
			case 200:
				return response;

			default:
				break;
			}
		}catch (final IOException e) {
		}finally {
			post.releaseConnection();
		}
		return null;
	}
}
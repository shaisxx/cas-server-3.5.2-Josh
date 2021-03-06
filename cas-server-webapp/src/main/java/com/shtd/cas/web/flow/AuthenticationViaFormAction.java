package com.shtd.cas.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.util.Constants;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;
  
/** 
 * Action to authenticate credentials and retrieve a TicketGrantingTicket for 
 * those credentials. If there is a request for renew, then it also generates 
 * the Service Ticket required. 
 *  
 * @author Scott Battaglia 
 * @version $Revision$ $Date$ 
 * @since 3.0.4 
 */  
@SuppressWarnings("deprecation")
public class AuthenticationViaFormAction {  
  
    /** 
     * Binder that allows additional binding of form object beyond Spring 
     * defaults. 
     */  
	private CredentialsBinder credentialsBinder;  
  
    /** Core we delegate to for handling all ticket related tasks. */  
    @NotNull  
    private CentralAuthenticationService centralAuthenticationService;  
  
    @NotNull  
    private CookieGenerator warnCookieGenerator;  
    
    @NotNull
    private DataSource dataSource;
    
	private SimpleJdbcTemplate jdbcTemplate;
    
    protected Logger logger = LoggerFactory.getLogger(getClass());  
  
    public final void doBind(final RequestContext context, final Credentials credentials) throws Exception {  
          
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);  
  
        if (this.credentialsBinder != null && this.credentialsBinder.supports(credentials.getClass())) {  
            this.credentialsBinder.bind(request, credentials);  
        }  
    }  
      
    public final String submit(final RequestContext context, final Credentials credentials, final MessageContext messageContext) throws Exception {  
        // Validate login ticket  
        final String authoritativeLoginTicket = WebUtils.getLoginTicketFromFlowScope(context);  
        final String providedLoginTicket = WebUtils.getLoginTicketFromRequest(context);  
        
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);  
        final HttpSession session = request.getSession();
          
        String username = null;
        if(credentials != null && ((UsernamePasswordCredentials)credentials).getUsername() != null){
        	username = ((UsernamePasswordCredentials)credentials).getUsername();
        }
        
        System.out.println(authoritativeLoginTicket+","+providedLoginTicket);  
        if (!authoritativeLoginTicket.equals(providedLoginTicket)) {  
            System.out.println("providedLoginTicket ERROR");  
            this.logger.warn("Invalid login ticket " + providedLoginTicket);  
            final String code = "INVALID_TICKET";  
            messageContext.addMessage(  
                new MessageBuilder().error().code(code).arg(providedLoginTicket).defaultText(code).build());  
            return "error";  
        }  
          
        final String ticketGrantingTicketId = WebUtils.getTicketGrantingTicketId(context);  
        final Service service = WebUtils.getService(context);  
        if (StringUtils.hasText(context.getRequestParameters().get("renew")) && ticketGrantingTicketId != null && service != null) {  
  
            try {  
                final String serviceTicketId = this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service, credentials);  
                WebUtils.putServiceTicketInRequestScope(context, serviceTicketId);  
                putWarnCookieIfRequestParameterPresent(context);  
                return "warn";  
            } catch (final TicketException e) {  
                if (isCauseAuthenticationException(e)) {  
                    populateErrorsInstance(e, messageContext);  
                    return getAuthenticationExceptionEventId(e);  
                }  
                  
                this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);  
                if (logger.isDebugEnabled()) {  
                    logger.debug("Attempted to generate a ServiceTicket using renew=true with different credentials", e);  
                }  
            }  
        }  
        
        try {  
        	
        	String ticketGrantingTicket = this.centralAuthenticationService.createTicketGrantingTicket(credentials);
        	session.setAttribute(ticketGrantingTicket, username);
        	
            WebUtils.putTicketGrantingTicketInRequestScope(context, ticketGrantingTicket);
            putWarnCookieIfRequestParameterPresent(context);  
            
            try {
    			getJdbcTemplate().update(Constants.LOG_INSERT_SQL, new Object[]{username==null?"--":username, "1", "用户登录", "1", (username==null?"用户":username) + "登录", WebUtils.getHttpServletRequest(context).getRemoteAddr()});
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
            
            return "success";  
            
        } catch (final TicketException e) {  
            populateErrorsInstance(e, messageContext);  
            
            String isAjaxStr = context.getRequestParameters().get("isajax");    
            if (!org.apache.commons.lang.StringUtils.isBlank(isAjaxStr)) {    
                System.out.println("errorForRemoteRequestor!!!!!!!!!!!!!!!!!!!");  
            }   
            
            try {
    			getJdbcTemplate().update(Constants.LOG_INSERT_SQL, new Object[]{username==null?"--":username, "1", "用户登录", "0", (username==null?"用户":username) + "登录", WebUtils.getHttpServletRequest(context).getRemoteAddr()});
    		} catch (Exception ex) {
    			ex.printStackTrace();
    		}
            
            return "error";   
        }  
    }  
  
    private void populateErrorsInstance(final TicketException e, final MessageContext messageContext) {  
        System.out.println("populateErrorsInstance");  
        try {  
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());  
        } catch (final Exception fe) {  
            logger.error(fe.getMessage(), fe);  
        }  
    }  
  
    private void putWarnCookieIfRequestParameterPresent(final RequestContext context) {  
        System.out.println("putWarnCookieIfRequestParameterPresent");  
        final HttpServletResponse response = WebUtils.getHttpServletResponse(context);  
  
        if (StringUtils.hasText(context.getExternalContext().getRequestParameterMap().get("warn"))) {  
            this.warnCookieGenerator.addCookie(response, "true");  
        } else {  
            this.warnCookieGenerator.removeCookie(response);  
        }  
    }  
      
    private AuthenticationException getAuthenticationExceptionAsCause(final TicketException e) {  
        return (AuthenticationException) e.getCause();  
    }  
  
    private String getAuthenticationExceptionEventId(final TicketException e) {  
        final AuthenticationException authEx = getAuthenticationExceptionAsCause(e);  
  
        if (this.logger.isDebugEnabled())  
            this.logger.debug("An authentication error has occurred. Returning the event id " + authEx.getType());  
  
        return authEx.getType();  
    }  
  
    private boolean isCauseAuthenticationException(final TicketException e) {  
        return e.getCause() != null && AuthenticationException.class.isAssignableFrom(e.getCause().getClass());  
    }  
  
    public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {  
        this.centralAuthenticationService = centralAuthenticationService;  
    }  
  
    /** 
     * Set a CredentialsBinder for additional binding of the HttpServletRequest 
     * to the Credentials instance, beyond our default binding of the 
     * Credentials as a Form Object in Spring WebMVC parlance. By the time we 
     * invoke this CredentialsBinder, we have already engaged in default binding 
     * such that for each HttpServletRequest parameter, if there was a JavaBean 
     * property of the Credentials implementation of the same name, we have set 
     * that property to be the value of the corresponding request parameter. 
     * This CredentialsBinder plugin point exists to allow consideration of 
     * things other than HttpServletRequest parameters in populating the 
     * Credentials (or more sophisticated consideration of the 
     * HttpServletRequest parameters). 
     * 
     * @param credentialsBinder the credentials binder to set. 
     */  
    public final void setCredentialsBinder(final CredentialsBinder credentialsBinder) {  
        this.credentialsBinder = credentialsBinder;  
    }  
      
    public final void setWarnCookieGenerator(final CookieGenerator warnCookieGenerator) {  
        this.warnCookieGenerator = warnCookieGenerator;  
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
} 
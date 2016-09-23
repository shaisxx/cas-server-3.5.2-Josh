package com.shtd.cas.web.flow;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.PasswordEqualException;
import org.jasig.cas.authentication.handler.PasswordIsNullException;
import org.jasig.cas.authentication.handler.PasswordMatchException;
import org.jasig.cas.authentication.handler.PasswordNotEqualException;
import org.jasig.cas.authentication.handler.PasswordNotMatchException;
import org.jasig.cas.authentication.handler.PasswordOneIsNullException;
import org.jasig.cas.authentication.handler.PasswordTwoIsNullException;
import org.jasig.cas.authentication.principal.ChangePasswordCredentials;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.util.CasUtility;
import org.jasig.cas.util.Constants;
import org.jasig.cas.web.bind.CredentialsBinder;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;

import com.shtd.cas.entity.SysUser;

/**
 * 修改密码 Action
 * @author Josh 
 * @date 20160909
 */
@SuppressWarnings("deprecation")
public class ChangePwdViaFormAction {  
  
	public static final String REGEX_VALIDATE_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[a-zA-Z0-9~!@#$%^&\\*()_+-=|\\:;\"'\\?/>\\.<,{\\[}\\]]{8,50}$";
	
	private CredentialsBinder credentialsBinder;  
  
    @NotNull  
    private CentralAuthenticationService centralAuthenticationService;  
    
    @NotNull
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;
  
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
    	
    	final HttpServletRequest request = WebUtils.getHttpServletRequest(context);  
    	final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
//    	final String service = request.getParameter("service");
    	
    	final HttpSession session = request.getSession();
    	
    	String username = (String)session.getAttribute(ticketGrantingTicketId);
    	
    	if (ticketGrantingTicketId != null) {
    		
            String orgPwd = null;
            String newPwd1 = null;
            String newPwd2 = null;
            if(credentials != null){
            	orgPwd = ((ChangePasswordCredentials)credentials).getOrgpwd();
            	newPwd1 = ((ChangePasswordCredentials)credentials).getPwd1();
            	newPwd2 = ((ChangePasswordCredentials)credentials).getPwd2();
            }
            
            try { 
            	
            	// 新密码不能为空
            	if (CasUtility.isBlank(orgPwd)) {
            		throw new PasswordIsNullException(); 
	    		}
            	
            	// 确认密码都不能为空
            	if (CasUtility.isBlank(newPwd1)) {
            		throw new PasswordOneIsNullException(); 
	    		}
            	
            	// 原密码、新密码、确认密码都不能为空
            	if (CasUtility.isBlank(newPwd2)) {
            		throw new PasswordTwoIsNullException(); 
	    		}
	    		
	    		// 新密码、确认密码要相同
	    		if (!newPwd1.equals(newPwd2)) {
	    			throw new PasswordNotEqualException(); 
	    		}
	    		
	    		// 新密码不能和当前密码相同
	    		if (orgPwd.equals(newPwd2)) {
	    			throw new PasswordEqualException(); 
	    		}
	    		
	    		// 新密码不满足规定
	    		if (!Pattern.compile(REGEX_VALIDATE_PWD).matcher(newPwd1).matches()) {
	    			throw new PasswordMatchException(); 
	    		}
	    		
	        	// 根据TGT获得登录用户名 Begin
//	    		String serviceTicket = null;
//	        	if(service != null && service.length() != 0){
	    		if(username != null && username.length() != 0){
		        	/*String casServerUrl = CasUtility.getCasServerUrl(request);
		    	    Cas20ProxyTicketValidator sv = new Cas20ProxyTicketValidator(casServerUrl);
		    	    sv.setAcceptAnyProxy(true);
		        	String server = CasUtility.getCasServerRestUrl(request);
		        	serviceTicket = getServiceTicket(server, ticketGrantingTicketId, service);
			        Assertion a = sv.validate(serviceTicket, service);
			        username = a.getPrincipal().getName();*/
			        
			        String salt = CasUtility.getSalt();
			        String password = CasUtility.encodePassword(newPwd1, salt);
			        
		    		String sql = "SELECT `code`,`password`,salt FROM sys_user WHERE `code` = '" + username + "'";
					List<SysUser> userList = getJdbcTemplate().query(sql, ParameterizedBeanPropertyRowMapper.newInstance(SysUser.class));
		    		
		            // 检验当前密码是否正确
		            if (userList != null &&  userList.size() > 0 && userList.get(0) != null && !isPwdMatched((SysUser)userList.get(0), orgPwd)) {
		            	throw new PasswordNotMatchException(); 
		            }
			        
			        try {
			        	getJdbcTemplate().update(Constants.CHANGE_PWD_SQL, new Object[]{password, salt, username});
			        	// 成功修改密码之后，注销TGT
			        	//this.centralAuthenticationService.destroyTicketGrantingTicket(ticketGrantingTicketId);  
			        	return "success";
		    		} catch (Exception ex) {
		    			ex.printStackTrace();
		    			return "error";   
		    		}
	        	}
	        	// 根据TGT获得登录用户名 End
        	
            } catch (final BadUsernameOrPasswordAuthenticationException e) {  
            	populateErrorsInstance(e, messageContext); 
            	return "error";
            }
    	}
    	return "error";   
    }
  
    private void populateErrorsInstance(final BadUsernameOrPasswordAuthenticationException e, final MessageContext messageContext) {  
        System.out.println("populateErrorsInstance");  
        try {  
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());  
        } catch (final Exception fe) {  
            logger.error(fe.getMessage(), fe);  
        }  
    }  
  
    public CookieRetrievingCookieGenerator getTicketGrantingTicketCookieGenerator() {
		return ticketGrantingTicketCookieGenerator;
	}

	public void setTicketGrantingTicketCookieGenerator(final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
		this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
	}

	public final void setCentralAuthenticationService(final CentralAuthenticationService centralAuthenticationService) {  
        this.centralAuthenticationService = centralAuthenticationService;  
    }  
  
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
	
	private boolean isPwdMatched(SysUser sysUser, String pwd) {
		if (sysUser == null || sysUser.getPassword() == null || sysUser.getPassword().length() == 0) {
			return false;
		}
		String encodePwd = CasUtility.encodePassword(pwd, sysUser.getSalt());
		if (!sysUser.getPassword().equals(encodePwd)) {
			return false;
		}
		return true;
	}
} 
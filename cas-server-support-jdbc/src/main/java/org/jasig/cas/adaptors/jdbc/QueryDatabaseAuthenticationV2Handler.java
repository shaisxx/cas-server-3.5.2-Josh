/**
 * 
 */
package org.jasig.cas.adaptors.jdbc;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.UnEnabledAuthenticationException;
import org.jasig.cas.authentication.handler.UnknownUsernameAuthenticationException;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 			shiro.applicationSalt=s4h8t3d6
			shiro.hashAlgorithmName=SHA-512
			shiro.storedCredentialsHexEncoded=false
			shiro.hashIterations=1024
 * @className:QueryDatabaseAuthenticationV2Handler.java
 * @author jiangnan
 * @createTime:2015/3/16 18:35
 * @description:  md5+salt 
 * 
 */
public class QueryDatabaseAuthenticationV2Handler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	
	//private static final String QUERY_USER_SQL = "select salt,password from sys_user where username = ?"
	//		+ " and status = 1 and logic_delete = 0  ";  
	
	private static final int USER_STATUS_DISABLED = 0;

	@NotNull
    private String sql;
 
	@NotNull
	private String  applicationSalt;
	@NotNull
	private String hashIterations;
	
	@Override
	protected boolean authenticateUsernamePasswordInternal(
			UsernamePasswordCredentials credentials)
			throws AuthenticationException {
		final String username = credentials.getUsername(); 
        final String password = credentials.getPassword();  
        JdbcTemplate template = new JdbcTemplate(getDataSource()); 
    	 Map<String, Object> mp= template.queryForMap(sql, username); 
    	 String salt=(String) mp.get("salt");
         String userPassword=(String) mp.get("password");
         Integer userStatus=(Integer) mp.get("status");
         
         String cPassword  = encodePassword(password, salt);
         if(userStatus.intValue() != USER_STATUS_DISABLED){
             if(userPassword.equals(cPassword)){
            	 return true;
             }else {
            	 throw new UnknownUsernameAuthenticationException();  
			}
         }else {
        	 throw new UnEnabledAuthenticationException();  
		}
	}
	 /**
     * @param sql The sql to set.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }
    
    public void setApplicationSalt(final String applicationSalt){
    	this.applicationSalt = applicationSalt;
    }
    
    public void setHashIterations(final String hashIterations){
    	this.hashIterations = hashIterations;
    }
    
    
    public String getApplicationSalt() {
		return applicationSalt;
	}

	public String getCombinedSalt(String salt) {
		return getApplicationSalt() + ":" + salt;
	}
	
	public Integer getIterations() {
		return Integer.parseInt(hashIterations);
	}

	public static String getSalt() {
		return new SecureRandomNumberGenerator().nextBytes().toBase64();
	}

	public String encodePassword(String password, String salt) {
		return new Sha512Hash(password, getCombinedSalt(salt), getIterations()).toBase64();
	}
	
 
}

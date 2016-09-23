package org.jasig.cas.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha512Hash;


public class CasUtility {
	
	/**
	 * CAS服务器的url
	 * @return
	 * @author Josh
	 */
	public static String getCasServerUrl(HttpServletRequest request){
		return "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + Constants.getCasConfigValue(Constants.CFG_KEY_CAS_SERVER_URL, "/cas/"); 
	}
	
	/**
	 * CAS服务器的rest url
	 * @return
	 * @author Josh
	 */
	public static String getCasServerRestUrl(HttpServletRequest request){
		return "http://" + request.getLocalAddr() + ":" + request.getLocalPort() + Constants.getCasConfigValue(Constants.CFG_KEY_CAS_SERVER_REST_URL, "/cas/v1/tickets"); 
	}
	
	public static String getApplicationSalt() {
		return Constants.getCasConfigValue("cas.applicationSalt", "s4h8t3d6");
	}

	public static String getCombinedSalt(String salt) {
		return getApplicationSalt() + ":" + salt;
	}
	
	public static Integer getIterations() {
		return Integer.parseInt(Constants.getCasConfigValue("cas.hashIterations", "1024"));
	}
	
	public static String getSalt() {
		return new SecureRandomNumberGenerator().nextBytes().toBase64();
	}
	
	public static String encodePassword(String password, String salt) {
		return new Sha512Hash(password, getCombinedSalt(salt), getIterations()).toBase64();
	}
	
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isCaptchaValidate(){
    	String isValidate = Constants.getCasConfigValue("cas.captcha.validate", "false");
    	if(isValidate != null && isValidate.equals("true")){
    		return true;
    	}else {
			return false;
		}
    }
}
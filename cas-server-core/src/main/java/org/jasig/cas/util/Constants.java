package org.jasig.cas.util;


public class Constants {
	
	//记录用户登录、退出cas sever日志 SQL语句
	public final static String LOG_INSERT_SQL = "INSERT INTO user_logon_log(code, operate_type, module_name, result_type, description, ip_addr, create_date) VALUES(?, ?, ?, ?, ?, ?, now())";

	public final static String CHANGE_PWD_SQL = "UPDATE sys_user SET `password` = ?, `salt` = ? WHERE `code` = ?";

	// cas.properties 中的参数
	public static final String CFG_KEY_CAS_SERVER_URL = "cas.server.url";
	public static final String CFG_KEY_CAS_SERVER_REST_URL = "cas.server.rest.url";
	
	public static final String CFG_KEY_CAS_CAPTCHA_VALIDATE = "cas.captcha.validate";
	
	//cas配置文件
	private static ConfigurationUtil CAS_CONFIG = new ConfigurationUtil("/../cas.properties");
	public static String getCasConfigValue(String key, String defaultVal) {
		String ret = CAS_CONFIG.getValue(key);
		if (ret == null || ret.length() == 0) {
			ret = defaultVal;
		}
		return ret;
	}
}
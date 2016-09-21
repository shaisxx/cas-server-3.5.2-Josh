package com.shtd.cas.entity;

import java.io.Serializable;

/**
 * @author Josh
 * @date 20160909
 */
public class SysUser  implements Serializable{

	private static final long serialVersionUID = 3943866616506001576L;

	private String code;
	/**
     * password 列的映射:密码 用户ID+密码+盐后的散列值(SHA-512)
     */
    private String password;
    
    /**
     * salt 列的映射:盐 随机数
     */
    private String salt;
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
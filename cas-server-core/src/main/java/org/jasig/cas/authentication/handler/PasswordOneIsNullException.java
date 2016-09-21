package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 原密码、新密码、确认密码都不能为空 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordOneIsNullException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordOneIsNullException ERROR = new PasswordOneIsNullException();

    private static final String CODE = "error.password.one.isnull";

    public PasswordOneIsNullException() {
        super(CODE);
    }

    public PasswordOneIsNullException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordOneIsNullException(final String code) {
        super(code);
    }

    public PasswordOneIsNullException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
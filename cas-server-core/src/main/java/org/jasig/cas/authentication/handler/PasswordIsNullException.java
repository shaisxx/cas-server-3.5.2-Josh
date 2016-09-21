package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 原密码、新密码、确认密码都不能为空 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordIsNullException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordIsNullException ERROR = new PasswordIsNullException();

    private static final String CODE = "error.password.isnull";

    public PasswordIsNullException() {
        super(CODE);
    }

    public PasswordIsNullException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordIsNullException(final String code) {
        super(code);
    }

    public PasswordIsNullException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
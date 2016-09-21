package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 原密码、新密码、确认密码都不能为空 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordTwoIsNullException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordTwoIsNullException ERROR = new PasswordTwoIsNullException();

    private static final String CODE = "error.password.two.isnull";

    public PasswordTwoIsNullException() {
        super(CODE);
    }

    public PasswordTwoIsNullException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordTwoIsNullException(final String code) {
        super(code);
    }

    public PasswordTwoIsNullException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 新密码不满足规定 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordMatchException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordMatchException ERROR = new PasswordMatchException();

    private static final String CODE = "error.password.match";

    public PasswordMatchException() {
        super(CODE);
    }

    public PasswordMatchException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordMatchException(final String code) {
        super(code);
    }

    public PasswordMatchException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
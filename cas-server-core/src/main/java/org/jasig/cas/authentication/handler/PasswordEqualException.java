package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 新密码不能和当前密码相同 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordEqualException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordEqualException ERROR = new PasswordEqualException();

    private static final String CODE = "error.password.equal";

    public PasswordEqualException() {
        super(CODE);
    }

    public PasswordEqualException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordEqualException(final String code) {
        super(code);
    }

    public PasswordEqualException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
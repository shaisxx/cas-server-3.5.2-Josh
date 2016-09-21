package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 新密码、确认密码要相同 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordNotEqualException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordNotEqualException ERROR = new PasswordNotEqualException();

    private static final String CODE = "error.password.notequal";

    public PasswordNotEqualException() {
        super(CODE);
    }

    public PasswordNotEqualException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordNotEqualException(final String code) {
        super(code);
    }

    public PasswordNotEqualException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
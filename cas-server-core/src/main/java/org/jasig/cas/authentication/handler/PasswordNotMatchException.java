package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 检验当前密码是否正确 Exception
 * @author Josh
 * @date 20160909
 */
public class PasswordNotMatchException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final PasswordNotMatchException ERROR = new PasswordNotMatchException();

    private static final String CODE = "error.password.notmatch";

    public PasswordNotMatchException() {
        super(CODE);
    }

    public PasswordNotMatchException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public PasswordNotMatchException(final String code) {
        super(code);
    }

    public PasswordNotMatchException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
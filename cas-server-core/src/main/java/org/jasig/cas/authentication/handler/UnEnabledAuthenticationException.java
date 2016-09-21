package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * @author Josh
 * @date 20160909
 */
public class UnEnabledAuthenticationException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = 1880452963335779810L;

	public static final UnEnabledAuthenticationException ERROR = new UnEnabledAuthenticationException();

    private static final String CODE = "error.authentication.credentials.bad.unenabled.status";

    public UnEnabledAuthenticationException() {
        super(CODE);
    }

    public UnEnabledAuthenticationException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public UnEnabledAuthenticationException(final String code) {
        super(code);
    }

    public UnEnabledAuthenticationException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
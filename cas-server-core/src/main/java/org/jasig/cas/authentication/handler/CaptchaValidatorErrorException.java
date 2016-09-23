package org.jasig.cas.authentication.handler;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;

/**
 * 验证码 Exception
 * @author Josh
 * @date 20160922
 */
public class CaptchaValidatorErrorException extends BadUsernameOrPasswordAuthenticationException{
    
	private static final long serialVersionUID = -2326715848254596163L;

	public static final CaptchaValidatorErrorException ERROR = new CaptchaValidatorErrorException();

    private static final String CODE = "error.captcha.validator";

    public CaptchaValidatorErrorException() {
        super(CODE);
    }

    public CaptchaValidatorErrorException(final Throwable throwable) {
        super(CODE, throwable);
    }

    public CaptchaValidatorErrorException(final String code) {
        super(code);
    }

    public CaptchaValidatorErrorException(final String code,
        final Throwable throwable) {
        super(code, throwable);
    }
}
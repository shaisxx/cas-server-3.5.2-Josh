package com.shtd.cas.web.flow;

import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.CaptchaValidatorErrorException;
import org.jasig.cas.util.CasUtility;
import org.jasig.cas.web.support.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaValidateAction{
	
	private ImageCaptchaService captchaService;
	private String captchaValidationParameter = "j_captcha_response";
	
	protected Logger logger = LoggerFactory.getLogger(getClass());  

	public final String doSubmit(final RequestContext context, final MessageContext messageContext) throws Exception{
		
		boolean isCaptchaValidate = CasUtility.isCaptchaValidate();

		if(isCaptchaValidate){
			String captcha_response = context.getRequestParameters().get(captchaValidationParameter);
			boolean valid = false;
	
			if (captcha_response != null) {
				String id = WebUtils.getHttpServletRequest(context).getSession().getId();
				if (id != null) {
					try {
						valid = captchaService.validateResponseForID(id, captcha_response).booleanValue();
					} catch (CaptchaServiceException cse) {
						logger.error(cse.getMessage(), cse);
					}
				}
			}
	
			if (valid) {
				return "success";  
			}else {
				try { 
					throw new CaptchaValidatorErrorException();
	            } catch (final BadUsernameOrPasswordAuthenticationException e) {  
	            	populateErrorsInstance(e, messageContext); 
	            	return "error";
	            }
			}
		}else {
			return "success";  
		}
	}

	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public void setCaptchaValidationParameter(String captchaValidationParameter) {
		this.captchaValidationParameter = captchaValidationParameter;
	}
	
    private void populateErrorsInstance(final BadUsernameOrPasswordAuthenticationException e, final MessageContext messageContext) {  
        System.out.println("populateErrorsInstance");  
        try {  
            messageContext.addMessage(new MessageBuilder().error().code(e.getCode()).defaultText(e.getCode()).build());  
        } catch (final Exception fe) {
        	logger.error(fe.getMessage(), fe);  
        }  
    } 
}

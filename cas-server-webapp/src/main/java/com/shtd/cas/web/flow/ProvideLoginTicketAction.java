package com.shtd.cas.web.flow;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.jasig.cas.util.CasUtility;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/** 
 * Opens up the CAS web flow to allow external retrieval of a login ticket. 
 *  
 * 如果参数中包含 get-lt 参数，则返回 loginTicketRequested 执行流，并跳转至 loginTicket 生成页，
 * 否则 则跳过该flow，并按照原始login的流程来执行。 
 * 
 * Modify by Josh at 20160923
 */  
public class ProvideLoginTicketAction extends AbstractAction {  
	
	/** 3.5.1 - Login tickets SHOULD begin with characters "LT-" */  
    private static final String PREFIX = "LT";  
  
    @NotNull  
    private UniqueTicketIdGenerator ticketIdGenerator;  
  
    public final String generate(final RequestContext context) {  
        final String loginTicket = this.ticketIdGenerator.getNewTicketId(PREFIX);  
        WebUtils.putLoginTicket(context, loginTicket);  
        return "generated";  
    }  
  
    public void setTicketIdGenerator(final UniqueTicketIdGenerator generator) {  
        this.ticketIdGenerator = generator;  
    }  
    
    @Override  
    protected Event doExecute(RequestContext context) throws Exception {  
          
        final HttpServletRequest request = WebUtils.getHttpServletRequest(context);  
          
        if (request.getParameter("get-lt") != null && request.getParameter("get-lt").equalsIgnoreCase("true")) {  
              
            final String loginTicket = this.ticketIdGenerator.getNewTicketId(PREFIX);  
            WebUtils.putLoginTicket(context, loginTicket);  
            
            return result("loginTicketRequested");
        }
        
        // Modify by Josh at 20160923.
        // 根据配置文件判断是否有验证码
        boolean isCaptchaValidate = CasUtility.isCaptchaValidate();
        WebUtils.putIsCaptchaValidate(context, isCaptchaValidate);
        
        return result("continue");  
    }  
}
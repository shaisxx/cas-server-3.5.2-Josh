<%@ page contentType="text/html; charset=UTF-8"%>  
<%  
    String ajax = request.getParameter("n");  
    //当执行Ajax自定义页面时执行以下操作  
    if(ajax!=null && ajax.length() > 0) {  
    	
    	String callbackName = request.getParameter("callback");  
        String jsonData = "{\"loginTicket\":\"" + request.getAttribute("loginTicket") + "\", \"flowExecutionKey\":\"" + request.getAttribute("flowExecutionKey") + "\"}";  
        String jsonp = callbackName + "(" + jsonData + ")";
        
    	response.setContentType("application/javascript");  
        response.getWriter().write(jsonp); 
        
    } else {  
      
    //正常cas执行  
%>         
     <script>window.location.href = "/cas/login";</script>  
<%         
    }  
%>
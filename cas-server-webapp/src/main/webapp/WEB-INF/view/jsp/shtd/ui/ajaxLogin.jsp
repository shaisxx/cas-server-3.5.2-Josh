<%@ page contentType="text/html; charset=UTF-8"%>  
<html>  
    <head>  
    </head>  
    <body>  
        <script type="text/javascript">  

            <%  
                Boolean isFrame = (Boolean)request.getAttribute("isFrame");  
                Boolean isLoginSucc = (Boolean)request.getAttribute("isLoginSucc");  
                if(isLoginSucc) {  
                    if(isFrame){%>  
                        parent.location.replace('${service}?ticket=${ticket}')  
                    <%} else{%>  
                        location.replace('${service}?ticket=${ticket}')  
                    <%}  
                }  
            %>  
            // callback 
            ${callback}({'login':${isLoginSucc ? '"success"': '"failed"'}});
        </script>  
    </body>  
</html>  
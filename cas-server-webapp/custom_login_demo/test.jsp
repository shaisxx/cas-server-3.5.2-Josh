<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">  
<html>  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  
<title>Cas登录</title>  
<script type="text/javascript" src="./jquery-1.11.0.min.js"></script>  
<script type="text/javascript" src="./jquery.form.min.js"></script>  
<script type="text/javascript" src="./messenger.js"></script>  
<script type="text/javascript">  
		function trim(stri) { return stri.replace(/(^\s*)|(\s*$)/g, ""); } 
		 
    $(document).ready(function(){   
        flushLoginTicket();  // 进入登录页，则获取login ticket，该函数在下面定义。  
        
    });  
  
    // 登录验证函数, 由 onsubmit 事件触发  
    var loginValidate = function(){  
        var msg;  
        if ($.trim($('#J_Username').val()).length == 0 ){  
            msg = "用户名不能为空。";  
        } else if ($.trim($('#J_Password').val()).length == 0 ){  
            msg = "密码不能为空。";  
        }  
        if (msg && msg.length > 0) {  
            $('#J_ErrorMsg').fadeOut().text(msg).fadeIn();  
            return false;  
            // Can't request the login ticket.  
        } else if ($('#J_LoginTicket').val().length == 0){  
            $('#J_ErrorMsg').text('服务器正忙，请稍后再试..');  
            return false;  
        } else {  
            // 验证成功后，动态创建用于提交登录的 iframe  
            $('body').append($('<iframe id="casloginframe"/>').attr({  
                style: "display:none;width:0;height:0",   
                id: "ssoLoginFrame",  
                name: "ssoLoginFrame",  
                src: "javascript:false;"  
            }));
             
            return true;  
        }  
    }
    
      
    // 登录处理回调函数，将由 iframe 中的页同自动回调  
    var casLoginCallback = function (result) {  
        customLoginCallBack(result);  
        deleteIFrame('#ssoLoginFrame');// 删除用完的iframe,但是一定不要在回调前删除，Firefox可能有问题的  
    };  
      
    // 自定义登录回调逻辑  
    var customLoginCallBack = function(result){  
        // 登录失败，显示错误信息  
        if (!result.login == 'failed') {  
            $('#J_ErrorMsg').fadeOut().text("登录失败。").fadeIn();  
            // 重新刷新 login ticket  
            flushLoginTicket();  
        } else {
            alert("登陆成功");  
        }  
    }  
  
    var deleteIFrame = function (iframeName) {  
        var iframe = $(iframeName);   
        if (iframe) { // 删除用完的iframe，避免页面刷新或前进、后退时，重复执行该iframe的请求  
            iframe.remove()  
        }  
    };  
  
    // 由于一个 login ticket 只允许使用一次, 当每次登录需要调用该函数刷新 lt  
    var flushLoginTicket = function(){  
        var _services = 'service=' + encodeURIComponent('http://192.168.1.205:8080/zfbbs/');  
        var casUrl = 'http://192.168.1.205:8080/cas-server-webapp/login?'+_services+'&get-lt=true&callback=?&n=' + new Date().getTime();  
                
        $.getJSON(casUrl,    
            function(response) {
            	  $('#J_LoginTicket').val(response.loginTicket);  
                $("#J_Execution").val(response.flowExecutionKey); 
            }
        );  
    };  
     
    var messenger = new Messenger('cas-login-parent', 'cas-login-result');
    messenger.listen(function (msg) {
    	  if (msg == 'cas-login-succ') {
    	  	alert('登录成功');
    	  	
    	  } else {
    	  	alert('登录失败');
    	  	
    	  }
    }); 
</script>  
</head>  
<body>  
    <form action="http://192.168.1.205:8080/cas-server-webapp/login" method="post" onsubmit="return loginValidate();" target="ssoLoginFrame">  
    <ul>  
        <li><span class="red" style="height:12px;" id="J_ErrorMsg"></span></li>  
  
        <li>  
            <em>用户名：</em>  
            <input name="username" id="J_Username" type="text" style="width: 180px" />  
        </li>  
        <li>  
            <em>密　码：</em>  
            <input name="password" type="password"  id="J_Password" style="width: 180px" />  
        </li>  
  
        <li class="mai">  
            <em>&nbsp;</em>  
            <input type="checkbox" name="rememberMe" id="rememberMe" value="true"/>  
            &nbsp;自动登录  
            <a href="/retrieve">忘记密码？</a>  
        </li>  
        <li>  
            <em>&nbsp;</em>  
            <input type="hidden" name="isajax" value="true" />  
            <input type="hidden" name="isframe" value="true" />  
            <input type="text" name="callback" value="casLoginCallback" />  
            <input type="text" name="lt" value="1" id="J_LoginTicket">  
            <input type="text" name="execution" id="J_Execution" value="" />  
            <input type="hidden" name="_eventId" value="submit" />  
            <input name="" type="submit" value="登录" />  
        </li>  
    </ul>  
</form>  
</body>  
</html>  
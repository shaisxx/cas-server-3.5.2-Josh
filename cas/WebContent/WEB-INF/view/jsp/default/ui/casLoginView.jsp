<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page session="true" %>
<%@ page pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
<!--<![endif]-->

<!-- BEGIN HEAD -->
<head>
<meta charset="utf-8" />
<title>单点登录系统- 登录</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta content="width=device-width, initial-scale=1.0" name="viewport" />
<meta content="" name="description" />
<meta content="" name="author" />

<link href="${pageContext.request.contextPath}/css/yyjx/login.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/yyjx/jquery.enplaceholder.js"></script>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/yyjx/favicon.ico"/>
</head>
<!-- END HEAD -->

<body>
	<div class="title">
		<div class="title-img">
			<img src="${pageContext.request.contextPath}/img/yyjx/login_tit.png"/>
		</div>
	</div>

	<div class="login-main">
		<div class="login-content">
			<div class="login-left-img">
				<img src="${pageContext.request.contextPath}/img/yyjx/login_left_img.jpg">
			</div>
			
			<div class="login_box">
				<div class="login_l_box"></div>
				<form:form class="login-form" commandName="${commandName}" htmlEscape="true" method="post" id="fm1">
					<div class="login_r_box">
						<p class="welcome">欢迎登录</p>
						
						<form:errors path="*" id="msg" cssClass="tishi" element="label"/>
						
						<c:if test="${not empty sessionScope.openIdLocalId}">
							<input type="hidden" id="username" name="username" value="${sessionScope.openIdLocalId}" />
						</c:if>						
		 				
						<div class="user">
							<c:if test="${empty sessionScope.openIdLocalId}">
							<spring:message code="screen.welcome.label.netid.accesskey" var="userNameAccessKey" />
							<label class="name-label"></label> 
							<form:input class="input_username" id="username" type="text" placeholder="请输入用户名" path="username" tabindex="1" accesskey="${userNameAccessKey}" autocomplete="false" htmlEscape="true" />
							</c:if>							
						</div>
						
						<div class="password">
							<spring:message code="screen.welcome.label.password.accesskey" var="passwordAccessKey"/>
							<label class="password-label"></label> 
							<form:input class="input_password" id="password" type="password"  placeholder="请输入密码" path="password" tabindex="2" accesskey="${passwordAccessKey}"  htmlEscape="true" autocomplete="off"/>
						</div>
						
						<c:if test="${not empty isCaptchaValidate and isCaptchaValidate eq true}">
						<div class="authcode_box">
							验证码<input class="input_authcode" name="j_captcha_response" type="text" />
							<img class="authcode_img" src="captcha.htm" onclick="javascript:window.location.reload();"/>
						</div>											
						</c:if>
						<div class="login_button_box">
							<input type="hidden" name="lt" value="${loginTicket}" />
							<input type="hidden" name="execution" value="${flowExecutionKey}" />
							<input type="hidden" name="_eventId" value="submit" />	
							<button type="submit" class="login_button" accesskey="l">登录</button>
						</div>
						
					</div>
				</form:form>
			</div>
		</div>
	</div>
	
	<script>
		$('#username,#password').placeholder({isUseSpan:true});
	</script>

</body>
</html>
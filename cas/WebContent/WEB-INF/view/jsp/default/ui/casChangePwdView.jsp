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
<title>单点登录系统- 修改密码</title>
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
				<img src="${pageContext.request.contextPath}/img/yyjx/login_left_img.jpg" >
			</div>
			<div class="password_edit_box">
				<div class="login_l_box"></div>
				<form:form class="form-horizontal" commandName="${commandName}" htmlEscape="true" method="post" id="fm1">
					<div class="password_edit_r_box">
						<p class="welcome">修改密码</p>
						<form:errors path="*" id="msg" cssClass="password_edit_tishi" element="label"/>
						
						<div class="user">
							原密码<form:input class="new_password l_margin" id="orgpwd" type="password" placeholder="" path="orgpwd" tabindex="2" htmlEscape="true" autocomplete="off"/>
						</div>
						<div class="password">
							新密码<form:input class="new_password l_margin" id="pwd1" type="password" placeholder="必须为数字与字母的组合，且不少于8位" path="pwd1" tabindex="2" htmlEscape="true" autocomplete="off"/>
						</div>
						<div class="password">
							确认密码 <form:input class="new_password" id="pwd2" type="password" placeholder="必须为数字与字母的组合，且不少于8位" path="pwd2" tabindex="2" htmlEscape="true" autocomplete="off"/>
						</div>
						<div class="password_edit_button_box">
							<input type="hidden" name="execution" value="${flowExecutionKey}" />
							<input type="hidden" name="_eventId" value="submit" />	
							<button type="submit" class="submit_btn" accesskey="l">保存</button>							
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
	
	<!--  
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
			
			<div class="password_edit_box">
				<div class="login_l_box"></div>
				<form:form class="form-horizontal" commandName="${commandName}" htmlEscape="true" method="post" id="fm1">
					<div class="password_edit_r_box">
						<p class="welcome">修改密码</p>
						
						<form:errors path="*" id="msg" cssClass="tishi" element="label"/>
						
						<div class="user">
							<spring:message code="screen.welcome.label.password.accesskey" var="orgpwdAccessKey"/>
							原密码<form:input class="old_password l_margin" type="password" placeholder="" path="orgpwd" tabindex="2" accesskey="${orgpwdAccessKey}" htmlEscape="true" autocomplete="off"/>
						</div>						
						
						<div class="password">
							<spring:message code="screen.welcome.label.password.accesskey" var="pwd1AccessKey"/>
							新密码<form:input class="new_password l_margin" type="password" placeholder="必须为数字与字母的组合，且不少于8位" path="pwd1" tabindex="2" accesskey="${pwd1AccessKey}" htmlEscape="true" autocomplete="off"/>
						</div>						
						
						<div class="password">
							<spring:message code="screen.welcome.label.password.accesskey" var="pwd2AccessKey"/>
							确认密码 <form:input class="new_password l_margin" type="password" placeholder="必须为数字与字母的组合，且不少于8位" path="pwd2" tabindex="2" accesskey="${pwd2AccessKey}" htmlEscape="true" autocomplete="off"/>
						</div>						
						
						<div class="password_edit_button_box">
							<input type="hidden" name="execution" value="${flowExecutionKey}" />
							<input type="hidden" name="_eventId" value="submit" />	
							<button type="submit" class="login_button" accesskey="l">保存</button>							
						</div>
					</div>
				</form:form>
			</div>			
		</div>
	</div>	
	-->
	
	<script>
		$('#orgpwd,#pwd1,#pwd2').placeholder({isUseSpan:true});
	</script>
</body>
</html>
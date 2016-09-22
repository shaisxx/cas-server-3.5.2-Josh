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
	<meta charset="utf-8"/>
	<title>单点登录系统 - 注销</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta content="width=device-width, initial-scale=1.0" name="viewport"/>
	<meta content="" name="description"/>
	<meta content="" name="author"/>
	
	<link href="${pageContext.request.contextPath}/css/yyjx/login.css" rel="stylesheet" type="text/css"/>
	
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/yyjx/favicon.ico"/>
</head>
<!-- END HEAD -->

<body>
	<div class="title">
		<div class="title-img">
			<img src="${pageContext.request.contextPath}/img/yyjx/login_tit.png"/>
		</div>
	</div>
	
	<div class="exit-main"> 
		<div class="login-content">
			<div class="login-left-img"><img src="${pageContext.request.contextPath}/img/yyjx/exit_img.jpg" ></div>
			<p class="relogin_text"><a href="#">点击重新登录</a></p>
		</div>
	</div>
	
</body>
</html>
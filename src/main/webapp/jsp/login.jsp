<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%--===========================================================================
JSTL core tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--===========================================================================
JSTL functions tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%--===========================================================================
JSTL i18n tag library.
===========================================================================--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
	<title>Login</title>

	<style>
		<%@include file="/static/css/bootstrap.min.css" %>
		<%@include file="/static/css/style.css"%>
		<%@include file="/static/js/jquery.min.js" %>
		<%@include file="/static/js/bootstrap.min.js" %>
	</style>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-10">
				<h3>Login</h3>
			</div>
			<div class="col-md-2">
				<a href="/registration">Register</a>
			</div>
		</div>
		<div class="row">
			<div class="col-md-4"></div>
			<div class="col-md-4">
				<form class="form-horizontal" role="form" method="POST" action="/login">
					<div>
						<input type="text" class="form-control" id="user_email" name="user_email"
							placeholder="Email"  value="${loginUserBean.email}">
						<c:if test="${not empty errors_login['error_email']}">
							<p class="alert alert-danger fade in"> <c:out value = "${errors_login['error_email']}"/></p>
						</c:if>
						<input type="password" class="form-control" id="user_password" name="user_password"
							placeholder="Password" >
						<c:if test="${not empty errors_login['error_password']}">
							<p class="alert alert-danger fade in"> <c:out value = "${errors_login['error_password']}"/></p>
						</c:if>
						<c:if test="${not empty errors_login['error_cannot_login']}">
							<p class="alert alert-danger fade in"> <c:out value = "${errors_login['error_cannot_login']}"/></p>
						</c:if>
					</div>
					<br>
					<button type="submit" class="btn btn-primary">Login</button>
				</form>
			</div>
			<div class="col-md-4"></div>
		</div>
	</div>

</body>
</html>
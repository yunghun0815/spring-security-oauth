<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>
	<c:if test="${error eq 'true'}">
		<p style="color: red">${message}</p>
	</c:if>
	<form action="/login" method="post">
		<input type="text" name="id">
		<input type="password" name="password">
		<input type="submit" value="로그인">
	</form>
	<div>
		<a href="/oauth2/authorization/google">Google</a>
		<a href="/oauth2/authorization/naver">Naver</a>
	</div>
</body>
</html>
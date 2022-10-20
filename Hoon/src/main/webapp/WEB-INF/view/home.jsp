<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메인 페이지</title>
</head>
<body>
	<h1>메인 페이지</h1>
	<a href="/member/login">로그인 페이지</a>
	<a href="/member/signup">회원가입</a>
	
	<sec:authorize access="isAuthenticated()">
		<p>정보출력 <sec:authentication property="principal"/></p>
		<a href="/logout">로그아웃</a>
	</sec:authorize>
	
	<sec:authorize access="hasRole('ROLE_ADMIN')">
		<a href="/admin/main">관리자 페이지</a>
	</sec:authorize>
	<hr>
</body>
</html>
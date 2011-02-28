<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:bundle basename="msg/auth/login">
<div>
<c:choose>
<c:when test="%{empty user}">
<div><fmt:message key="login"/><input type="text" name="user_login"/></div>
<div><fmt:message key="password"/><input type="text" name="user_password"/></div>
<div align="right"><fmt:message key="enter"/></div>
</c:when>
<c:otherwise>
</c:otherwise>
</c:choose>
</div>
</fmt:bundle>
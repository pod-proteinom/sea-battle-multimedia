<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:bundle basename="msg/auth/login">
<div id="login_box">
<c:if test="${not empty requestScope['Authorization.LOGIN_ERROR']}">
<span class="error_msg"><fmt:message key="login_error"/></span>
</c:if>
<c:choose>
<c:when test="${empty current_user}">
<form action="" method="post">
<div><fmt:message key="login"/><input type="text" name="user_login"/></div>
<div><fmt:message key="password"/><input type="password" name="user_password"/></div>
<div align="right"><input type="submit" value="<fmt:message key="enter"/>"/></div>
</form>
<div><a href="${pageContext.servletContext.contextPath}/register.htm"><fmt:message key="register"/></a></div>
<div><a href="${pageContext.servletContext.contextPath}/password_restore.htm"><fmt:message key="password_restore"/></a></div>
</c:when>
<c:otherwise>
<div>
<div><fmt:message key="hello"/>, ${current_user.name}.</div>
<div><a href="${pageContext.servletContext.contextPath}/index.htm?logout"><fmt:message key="exit"/></a></div>
</div>
</c:otherwise>
</c:choose>
</div>
</fmt:bundle>
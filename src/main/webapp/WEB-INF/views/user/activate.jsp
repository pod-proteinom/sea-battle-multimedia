<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="msg/activate">
<div align="center" class="form_head"><fmt:message key="header"/></div>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<div align="center" class="content_msg">
<a href="${pageContext.servletContext.contextPath}/index.htm"><fmt:message key="back_link"/></a>
</div>

</fmt:bundle>
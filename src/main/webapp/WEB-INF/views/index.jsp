<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="msg/index">
<div align="center" class="form_head"><fmt:message key="header"/></div>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<div class="content_msg" align="center">
<a href="game/player.htm"><fmt:message key="player"/></a>
</div>
<div class="content_msg" align="center">
<a href="game/computer.htm"><fmt:message key="computer"/></a>
</div>

</fmt:bundle>
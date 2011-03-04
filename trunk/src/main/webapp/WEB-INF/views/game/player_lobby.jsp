<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/seabattle/dwr/engine.js"></script>
<script type="text/javascript" src="/seabattle/dwr/util.js"></script>
<script type="text/javascript" src="/seabattle/dwr/interface/TicketRemoteService.js"></script>

<fmt:bundle basename="msg/game/player">

<script type="text/javascript">
	window.onload=function()
	{
	    //dwr.engine.setActiveReverseAjax(true); // Initiate reverse ajax polling
	    //dwr.engine.setErrorHandler(errorHandler); // Optional - Called when a call and all retry attempts fail
		initReverseAjax();
	}

	function initReverseAjax() {
		if (!dwr.engine._scriptSessionId) {
			setTimeout("initReverseAjax()", 500); //some dirty hack :(
		} else {
			dwr.engine.setActiveReverseAjax(true);
			dwr.engine.setNotifyServerOnPageUnload(true);
			waitForPlayer();
		}  
	}

	function waitForPlayer(){
		TicketRemoteService.waitForPlayer();
	}

	function errorHandler(message, ex) {
	    dwr.util.setValue("dwr_error", "<font color='red'>Cannot connect to server. Initializing retry logic.</font>", {escapeHtml:false});
	    //setTimeout(function() { dwr.util.setValue("error", ""); }, 5000)
	}

	function oponentFound(){
		$("#main_msg").html('<fmt:message key="opponent_found"/>');
		setTimeout("redirectUser()", 1000);
	}

	function redirectUser(){
		window.location="?";
	}
</script>

<div align="center" class="form_head"><fmt:message key="header"/></div>

<div align="center" id="dwr_error"></div>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<div align="center" class="content_msg"><fmt:message key="${content_data}"/></div>

<div align="left" class="content_msg" id="main_msg"><fmt:message key="time_wait"/> : [<span id="waitTimer"></span>]</div>

</fmt:bundle>
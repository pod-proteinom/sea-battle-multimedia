<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/seabattle/dwr/engine.js"></script>
<script type="text/javascript" src="/seabattle/dwr/util.js"></script>
<script type="text/javascript" src="/seabattle/dwr/interface/RoundRemoteService.js"></script>

<script type="text/javascript" src="/seabattle/scripts/game.js"></script>
<script type="text/javascript" src="/seabattle/scripts/myships.js"></script>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<fmt:bundle basename="msg/game/player">
<script type="text/javascript">
$(function() {
	drawContent( "div#content1", "1", false);
	drawContent( "div#content2", "2", true);
	drawShips("myShips.htm", "1");
	drawShots({opponent:true, postfix:"1"});
	drawShots({opponent:false, postfix:"2"});
});
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
		waitRound();
	}  
}

function waitRound(){
	RoundRemoteService.waitRound();
}

function errorHandler(message, ex) {
    dwr.util.setValue("dwr_error", "<font color='red'>Cannot connect to server. Initializing retry logic.</font>", {escapeHtml:false});
    //setTimeout(function() { dwr.util.setValue("error", ""); }, 5000)
}

<fmt:bundle basename="msg/ships/shoot">
function myRound() {
	$("#main_msg1").html('<fmt:message key="YOUR_TURN"/>');
	$("#operation_result").html("&nbsp;");
	drawShots({opponent:true, postfix:"1"});
	drawShots({opponent:false, postfix:"2"});
}
function hisRound() {
	$("#main_msg1").html('<fmt:message key="HIS_TURN"/>');
	$("#operation_result").html("&nbsp;");
	drawShots({opponent:true, postfix:"1"});
	drawShots({opponent:false, postfix:"2"});
}
function win() {
	$("#main_msg1").html('<fmt:message key="WIN"/>');
	$("#operation_result").html("&nbsp;");
}
function loose() {
	$("#main_msg1").html('<fmt:message key="LOOSE"/>');
	$("#operation_result").html("&nbsp;");
}
function hit() {
	$("#main_msg2").html('<fmt:message key="HIT"/>');
}
function miss() {
	$("#main_msg2").html('<fmt:message key="MISS"/>');
}
function kill() {
	$("#main_msg2").html('<fmt:message key="KILL"/>');
}
</fmt:bundle>
</script>

<div align="center" class="form_head"><fmt:message key="header"/></div>

<div align="center" id="dwr_error"></div>

<div align="center" id="operation_result" class="content_msg"><fmt:message key="started_game"/></div>

<div align="center" id="main_msg1"></div>
<div align="center" id="main_msg2"></div>

<table>
<tr valign="top"><td>
<div id="content1">
<div class="field">
<div class="hnums"></div>
<div class="vnums"></div>
<div class="inner"></div>
</div>
</div>
</td><td>
<div id="content2">
<div class="field">
<div class="hnums"></div>
<div class="vnums"></div>
<div class="inner"></div>
</div>
</div>
</td></tr>
</table>

</fmt:bundle>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/seabattle/dwr/engine.js"></script>
<script type="text/javascript" src="/seabattle/dwr/util.js"></script>

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
});
</script>

<div align="center" class="form_head"><fmt:message key="header"/></div>

<div align="center" id="dwr_error"></div>

<div align="center" class="content_msg"><fmt:message key="started_game"/></div>

<div align="center" id="operation_result"></div>

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
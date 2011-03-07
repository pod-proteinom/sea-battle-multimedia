<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/seabattle/dwr/engine.js"></script>
<script type="text/javascript" src="/seabattle/dwr/util.js"></script>

<script type="text/javascript" src="/seabattle/scripts/battlefield.js"></script>
<script type="text/javascript" src="/seabattle/scripts/ships.js"></script>

<fmt:bundle basename="msg/game/player">

<div align="center" class="form_head"><fmt:message key="header"/></div>

<div align="center" id="dwr_error"></div>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<div align="center" class="content_msg"><fmt:message key="starting_game"/></div>

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
<div id="content2" style="display: none;">
<div class="field">
<div class="hnums"></div>
<div class="vnums"></div>
<div class="inner"></div>
</div>
</div>
</td><td>
<div><fmt:message key="current_ship"/></div>
<div id="currentShips"><fmt:message key="nothing"/></div>
<div id="availableShips">
</div>
</td></tr>
</table>

</fmt:bundle>
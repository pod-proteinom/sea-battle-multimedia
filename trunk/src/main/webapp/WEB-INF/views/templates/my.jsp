<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>template</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/styles-my.css">
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/jquery-ui.css">
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/scripts/jquery-1.5.min.js"></script>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/scripts/jquery-ui.min.js"></script>
</head>

<body>
<div>
<div class="locales">
<a href="?locale=ru"><img alt="ru" src="${pageContext.servletContext.contextPath}/images/i18n/ru.jpg"/></a>
<a href="?locale=en"><img alt="en" src="${pageContext.servletContext.contextPath}/images/i18n/en.jpg"/></a>
</div>
<div class="login">
<jsp:include page="/WEB-INF/jspf/auth/login.jsp" flush="false"/>
</div>
</div>
<br style="clear: both;"/>


<div>
<jsp:include page="${navigation_url}" flush="false"/>
</div>

<div>
<jsp:include page="${content_url}" flush="false"/>
</div>

</body>
</html>
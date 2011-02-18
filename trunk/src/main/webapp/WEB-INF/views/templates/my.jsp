<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>template</title>
<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/styles-my.css">
</head>
<body>

<div>
<jsp:include page="${navigation_url}" flush="false"/>
</div>

<jsp:include page="${content_url}" flush="false"/>

</body>
</html>
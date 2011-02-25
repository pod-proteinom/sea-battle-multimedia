<%-- this page simply displays all errors from param.error_message request attributes--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${requestScope['common_error_message'] ne null}">
<fmt:bundle basename="msg/errors">
<c:forEach items="${requestScope['common_error_message']}" var="err_msg">
<div class="error_msg"><fmt:message key="${err_msg}"/></div>
</c:forEach>
</fmt:bundle>
</c:if>
<%-- this page simply displays all errors from param.help_message request attributes--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${requestScope['common_help_message'] ne null}">
<fmt:bundle basename="msg/help">
<c:forEach items="${requestScope['common_help_message']}" var="hlp_msg">
<div class="help_msg"><fmt:message key="${hlp_msg}"/></div>
</c:forEach>
</fmt:bundle>
</c:if>
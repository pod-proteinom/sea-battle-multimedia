<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:bundle basename="msg/register">
<script type='text/javascript' src='${pageContext.servletContext.contextPath}/dwr/engine.js'></script>
<script type='text/javascript' src='${pageContext.servletContext.contextPath}/dwr/util.js'></script>

<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/autocomplete.css"/>
<script type="text/javascript" src="${pageContext.servletContext.contextPath}/scripts/jquery.autocomplete.js"></script>

<script type="text/javascript">
function valueChanged(id) {
    var received2 = function(data) {
        var img_name = id + '_img';
        if (data) {
            //alert(id+"<--"+data);
        	$('#'+id+'_error').html(data);
            $('#'+id+'_img').src = '${pageContext.servletContext.contextPath}/images/s_error.png';
        } else {
            //alert(id+"-->"+data);
        	$('#'+id+'_error').html('');
            $('#'+id+'_img').src = '${pageContext.servletContext.contextPath}/images/s_okay.png';
        }
    };
    var value = dwr.util.getValue(id);
    if (id == 'login') {
    	$.ajax({
    		url: 'ajax/validateLogin.htm?login='+value,
    		dataType: 'html',
    		success: received2
    	});
    } else if (id == 'email') {
    	$.ajax({
			url: 'ajax/validateEmail.htm?email='+value,
			dataType: 'html',
			success: received2
		});
    } else if (id == 'password') {
    	$.ajax({
			url: 'ajax/validatePassword.htm?password='+value,
			dataType: 'html',
			success: received2
		});
    } else if (id == 'password_repeat') {
    	$.ajax({
			url: 'ajax/validatePasswordRepeat.htm?password='+$("#password").val()+'&repeat='+value,
			dataType: 'html',
			success: received2
		});
    }
}
</script>

<%@include file="/WEB-INF/jspf/messages/help.jsp"%>
<%@include file="/WEB-INF/jspf/messages/error.jsp"%>

<div align="center" class="form_head"><fmt:message key="header"/></div>
<br>
<div align="center">* - <fmt:message key="required"/></div>
<form:form method="post" commandName="content_data" action="?action=register" >
<table align="center">
    <tr align="right"><td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Отмена">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Сохранить">
    </td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="name"/>&nbsp;*:&nbsp;<form:errors  path="name" cssClass="error_msg"/><br>
		</td></tr>
		<tr><td align="center">
			<form:input path="name" cssClass="long_text_area"/>
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="surname"/>&nbsp;*:&nbsp;<form:errors path="surname" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:input path="surname" cssClass="long_text_area"/>
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="country"/>&nbsp;*:
		<span id="country_error" class='error_msg'><form:errors path="country" cssClass="error_msg"/></span><br>
		</td></tr>
		<tr><td align="center">
			<input id="country_id" type="hidden" name="country.id" value="${content_data.country.id}" class="long_text_area"/>
			<input id="autocomplete1" autocomplete="off" value="${content_data.country.name}" style="width: 178px;" class="ac_input">
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="login"/>&nbsp;*:
		<span id="login_error" class='error_msg'><form:errors path="login" cssClass="error_msg"/></span><br>
		</td></tr>
		<tr><td align="center">
			<form:input path="login" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="login_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="password"/>&nbsp;*:
		<span id="password_error" class='error_msg'><form:errors path="password" cssClass="error_msg"/></span><br>
		</td></tr>
		<tr><td align="center">
			<form:password path="password" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="password_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="password_repeat"/>&nbsp;*:
		<span id="password_repeat_error" class='error_msg'><form:errors path="password_repeat" cssClass="error_msg"/><br></span>
		</td></tr>
		<tr><td align="center">
			<form:password path="password_repeat" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="password_repeat_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="email"/>&nbsp;*:
		<span id="email_error" class='error_msg'><form:errors path="email" cssClass="error_msg"/><br></span>
		</td></tr>
		<tr><td align="center">
			<form:input path="email" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="email_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold><fmt:message key="date"/>&nbsp;*:
		<span id="date_error" class='error_msg'><form:errors path="date" cssClass="error_msg"/><br></span>
		</td></tr>
		<tr><td align="center">
			<form:input path="date" cssClass="long_text_area"/>
		</td></tr>
    </table></td></tr>
    <tr align="right"><td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Отмена">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Сохранить">
    </td></tr>
</table>
</form:form>

</fmt:bundle>

<script type="text/javascript">
$("#date").datepicker({
			dateFormat: 'yy-mm-dd',
			onSelect: function(){
	        	$('#date_error').html('');
			}
		});

$("#autocomplete1").autocomplete({
	url:'${pageContext.servletContext.contextPath}/json/countries.htm',
	type:'json',
	values : true,
	writable : false,
    dataHandler : function(mask){
    	var self = this;
        return function(i, n) {
			if (n.name){
				self.cache[mask].push(n.name);
				self.store[mask] += self.mark(n.name,mask);
				if(self.values && !self.pairs[n.name]) self.pairs[n.name] = n.id;
			} else {
				self.cache[mask].push(n);
				self.store[mask] += self.mark(n,mask);
				if(self.values && !self.pairs[n]) self.pairs[n] = i;
			}
        };
	},
	onSelect:function() {
		$("#country_id").val(this.pairs[this.ac.val()]);
		$("#country_error").html('');
	}
});
$("#autocomplete1").bind('keyup', function() {
	$("#country_id").val('');
});
</script>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<link rel="stylesheet" type="text/css" href="${pageContext.servletContext.contextPath}/styles/countries.css"/>

<script type='text/javascript'
    src='${pageContext.servletContext.contextPath}/dwr/interface/UserValidator.js'></script>
<script type='text/javascript' src='${pageContext.servletContext.contextPath}/dwr/engine.js'></script>
<script type='text/javascript' src='${pageContext.servletContext.contextPath}/dwr/util.js'></script>

<script type='text/javascript' src='${pageContext.servletContext.contextPath}/scripts/countries.js'></script>

<script type="text/javascript">
function valueChanged(id) {
    var callback = function(res) {
        var img_name = id + '_img';
        if (res) {
            dwr.util.byId(img_name).src = '${pageContext.servletContext.contextPath}/images/s_error.png';
            dwr.util.byId(img_name).title = res;
        } else {
            dwr.util.byId(img_name).src = '${pageContext.servletContext.contextPath}/images/s_okay.png';
        }
    };
    var value = dwr.util.getValue(id);
    if (id == 'login') {
        UserValidator.validateLogin(value, callback);
    } else if (id == 'email') {
        UserValidator.validateEmail(value, callback);
    } else if (id == 'password') {
        UserValidator.validatePassword(value, callback);
    } else if (id == 'password_repeat') {
        UserValidator.validatePassword_repeat(dwr.util.getValue('password'), value, callback);
    }
}
</script>

<div align="center" class="form_head">Регистрация пользователя</div>
<br>
<div align="center">* - обязательно заполнить</div>
<form:form method="post" commandName="content_data" action="?action=register" >
<table align="center">
    <tr align="right"><td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Отмена">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Сохранить">
    </td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Имя&nbsp;*:&nbsp;<form:errors path="name" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:input path="name" cssClass="long_text_area"/>
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Фамилия&nbsp;*:&nbsp;<form:errors path="surname" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:input path="surname" cssClass="long_text_area"/>
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Страна&nbsp;*:&nbsp;<form:errors path="country" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:hidden path="country.id" cssClass="long_text_area"/>
			<form:input path="country.name" cssClass="long_text_area" onkeyup="lookupCountries(this.value)"/>
			<div class="suggestionsBox" id="suggestions" style="display: none;">
			<div class="suggestionList" id="suggestionsList"></div>
			</div>
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Логин&nbsp;*:&nbsp;<form:errors path="login" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:input path="login" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="login_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Пароль&nbsp;*:&nbsp;<form:errors path="password" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:password path="password" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="password_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>Пароль повтор&nbsp;*:&nbsp;<form:errors path="password_repeat" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:password path="password_repeat" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="password_repeat_img" />
		</td></tr>
    </table></td></tr>
    <tr><td align="center"><table>
		<tr><td class=bold>E-mail&nbsp;*:&nbsp;<form:errors path="email" cssClass="error_msg"/><br></td></tr>
		<tr><td align="center">
			<form:input path="email" cssClass="long_text_area" onchange="valueChanged(this.id);"/>
			<img src="" id="email_img" />
		</td></tr>
    </table></td></tr>
    <tr align="right"><td>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="Отмена">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="Сохранить">
    </td></tr>
</table>
</form:form>
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localHeader.jsp"%>

<form method="post">
	Get forms from: <input type="text" size="40" name="server" value="http://69.91.227.31/openmrs"/> <br/>
	Username: <input type="text" name="username" value="admin"/> <br/>
	Password: <input type="password" name="password" value=""/> <br/>
	Form UUIDs to get: <textarea name="uuids"></textarea> <br/>
	<br/>
	<input type="submit" value="Update my forms"/>
</form>

<%@ include file="/WEB-INF/template/footer.jsp"%>
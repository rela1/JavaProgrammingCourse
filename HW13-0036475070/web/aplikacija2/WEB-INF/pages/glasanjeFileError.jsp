<%@ page import="java.lang.String"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String bgColor = (String) request.getSession().getAttribute(
			"pickedBgCol");

	if (bgColor == null)
		bgColor = "WHITE";
%>

<html>
<body BGCOLOR="<%=bgColor%>">
<html>
<body>
	<h1>Pogreška prilikom glasanja!</h1>
	<p>Pogreška prilikom pristupa internim fileovima, glas nije
		zabilježen.</p>
	<a href="index.jsp">Index</a>
</body>
</html>
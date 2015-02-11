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
	<h1>Pogreška prilikom procesiranja zahtjeva!</h1>
	<p>
		Predani su krivi parametri! <br> "a" treba biti cijeli broj iz
		intervala [-100,100], <br>"b" treba biti cijeli broj iz intervala
		[-100,100] <br>i "n" treba biti cijeli broj iz intervala [1,5].
	</p>
	<br>
	<a href="index.jsp">Index</a>
</body>
</html>
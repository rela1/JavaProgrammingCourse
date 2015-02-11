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
	<h1>OS korištenje</h1>
	<p>Rezultat korištenja OS-ova iz ankete koju smo odradili.</p>
	<img alt="OS usage pie chart" src="/aplikacija2/reportImage">
	<br>
	<a href="index.jsp">Index</a>
</body>
</html>
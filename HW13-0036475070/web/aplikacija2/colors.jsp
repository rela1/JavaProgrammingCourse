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
	<a href="setcolor?color=WHITE">BIJELA</a>
	<br>
	<a href="setcolor?color=RED">CRVENA</a>
	<br>
	<a href="setcolor?color=GREEN">ZELENA</a>
	<br>
	<a href="setcolor?color=CYAN">CYAN</a>
	<br>
	<a href="index.jsp">Index</a>
</body>
</html>
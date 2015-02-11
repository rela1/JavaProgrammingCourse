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
	<h1>Glasanje za omiljeni bend:</h1>
	<p>Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na
		link kako biste glasali!</p>
	<ol>
		<c:forEach var="r" items="${artists}">
			<li><a href="glasanje-glasaj?id=${r.key}">${r.value}</a></li>
		</c:forEach>
	</ol>
	<a href="index.jsp">Index</a>
</body>
</html>
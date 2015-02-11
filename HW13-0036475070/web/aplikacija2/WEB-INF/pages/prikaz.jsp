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
	<a href="index.jsp">Index</a>
	<table border=5 color=BLACK>
		<thead>
			<tr>
				<th>Broj</th>
				<th>Kvadratna vrijednost</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="r" items="${rezultati}">
				<tr>
					<td>${r.broj}</td>
					<td>${r.vrijednost}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<a href="index.jsp">Index</a>
</body>
</html>
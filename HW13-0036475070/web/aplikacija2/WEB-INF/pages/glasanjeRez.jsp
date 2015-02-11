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
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Bend</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="v" items="${voteMap}">
				<tr>
					<td>${v.key}</td>
					<td>${v.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/aplikacija2/glasanje-grafika" width="400"
		height="400" />
	<br>
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="/aplikacija2/glasanje-xls">ovdje</a>
	</p>
	<br>
	<h2>Razno</h2>
	<p>Primjeri pjesama pobjedničkih bendova:</p>
	<ul>
		<c:forEach var="m" items="${maxVotes}">
			<li><a href="${m.value}" target="_blank">${m.key}</a></li>
		</c:forEach>
	</ul>
	<br>
	<a href="index.jsp">Index</a>
</body>
</html>
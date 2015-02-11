<%@ page import="java.lang.String"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
	<h1>Rezultati glasanja</h1>
	<p>Ovo su rezultati glasanja.</p>
	<a href="/aplikacija5/index.html">Index</a>
	<table border="1" cellspacing="0" class="rez">
		<thead>
			<tr>
				<th>Opcija</th>
				<th>Broj glasova</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="p" items="${pollEntries}">
				<tr>
					<td>${p.optionTitle}</td>
					<td>${p.votesCount}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>Grafički prikaz rezultata</h2>
	<img alt="Pie-chart" src="/aplikacija5/glasanje-grafika?pollID=${pollID}" width="600"
		height="400" />
	<br>
	<h2>Rezultati u XLS formatu</h2>
	<p>
		Rezultati u XLS formatu dostupni su <a
			href="/aplikacija5/glasanje-xls?pollID=${pollID}">ovdje</a>
	</p>
	<br>
	<h2>Razno</h2>
	<p>Dodatni sadržaj za opcije s najvećim brojem glasova:</p>
	<ul>
		<c:forEach var="m" items="${maxPollEntries}">
			<li><a href="${m.optionLink}" target="_blank">${m.optionTitle}</a></li>
		</c:forEach>
	</ul>
	<br>
	<a href="/aplikacija5/index.html">Index</a>
</body>
</html>
<%@ page import="hr.fer.zemris.web.radionice.User"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	User user = (User) request.getSession()
			.getAttribute("current.user");
%>
<html>
<head>
<title>Radionice</title>
<style type="text/css">
.topcorner {
	position: absolute;
	top: 0;
	right: 15;
}
</style>
</head>

<body>
	<h1>Lista definiranih radionica</h1>
	<div class="topcorner">
		<%
			if (user != null) {
		%>
		<p><%=user.getIme()%>
			<%=user.getPrezime()%>, <a
				href="${pageContext.servletContext.contextPath}/logout"> odjava</a>
		</p>
		<%
			} else {
		%>
		<p>
			Anonimni korisnik, <a
				href="${pageContext.servletContext.contextPath}/login"> prijava</a>
		</p>
		<%
			}
		%>
	</div>
	<c:choose>
		<c:when test="${radionice.isEmpty()}">
			<p>Trenutno nema evidentiranih radionica.</p>
		</c:when>
		<c:otherwise>
			<table border=5 color=BLACK>
				<thead>
					<tr>
						<th>Datum održavanja radionice</th>
						<th>Naziv radionice</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="radionica" items="${radionice}">
						<tr>
							<td>${radionica.datum}</td>
							<%
								if (user != null) {
							%>
							<td><a
								href="${pageContext.servletContext.contextPath}/edit?id=${radionica.id}">${radionica.naziv}</a></td>
							<%
								} else {
							%>
							<td>${radionica.naziv}</td>
							<%
								}
							%>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>
	</c:choose>

	<p>
		<%
			if (user != null) {
		%>
		<a href="${pageContext.servletContext.contextPath}/new">Nova
			radionica</a>
		<%
			}
		%>
	</p>
</body>
</html>

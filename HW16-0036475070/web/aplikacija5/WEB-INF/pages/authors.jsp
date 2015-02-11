<%@ page import="hr.fer.zemris.web.aplikacija5.model.BlogUser"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Autori blogova</title>
<style type="text/css">
.topcorner {
	position: absolute;
	top: 0;
	right: 15;
}

.greska {
	font-family: monospace;
	font-weight: bold;
	font-size: 0.9em;
	color: #FF0000;
}

.opis {
	text-align: right;
	font-family: sans-serif;
	font-style: italic;
	font-size: 0.9em;
}

td {
	vertical-align: top;
	width: auto;
	white-space: nowrap;
}

table {
	border-spacing: 7px;
}
</style>
</head>

<body>
	<h1>Lista registriranih autora</h1>
	<c:choose>
		<c:when test="${empty sessionScope['current.user.id']}">
			<h2>Prijava korisnika</h2>
			<div class="topcorner">
				<p>Korisnik nije prijavljen.</p>
			</div>
			<form
				action="${pageContext.servletContext.contextPath}/servleti/login"
				method="post">
				<table>
					<tr>
						<td><div class="opis">Korisničko ime</div></td>
						<td><input type="text" name="username" size="50"
							value="${user.nickname}"><br> <c:if
								test="${user.hasErrors('nickname')}">
								<div class="greska">
									<c:out value="${user.getError('nickname')}"></c:out>
								</div>
							</c:if></td>
					</tr>
					<tr>
						<td><div class="opis">Lozinka</div></td>
						<td><input type="password" name="password" size="50"><br>
							<c:if test="${user.hasErrors('password')}">
								<div class="greska">
									<c:out value="${user.getError('password')}"></c:out>
								</div>
							</c:if></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="submit" name="metoda" value="Prijavi">
					</tr>
				</table>
			</form>
			<a href="${pageContext.servletContext.contextPath}/servleti/register">Registracija
				novih korisnika</a>
		</c:when>
		<c:otherwise>
			<div class="topcorner">
				<p>
					<c:out value="${sessionScope['current.user.fn']}"></c:out>
					<c:out value="${sessionScope['current.user.ln']}"></c:out>
					, <a
						href="${pageContext.servletContext.contextPath}/servleti/logout">
						odjava</a>.
				</p>
			</div>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${users.isEmpty()}">
			<p>Trenutno nema evidentiranih autora blogova.</p>
		</c:when>
		<c:otherwise>
			<p>Lista registriranih autora blogova:</p>
			<ul>
				<c:forEach var="u" items="${users}">
					<li><a
						href="${pageContext.servletContext.contextPath}/servleti/author/${u.nick}">${u.nick}</a></li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
</body>
</html>

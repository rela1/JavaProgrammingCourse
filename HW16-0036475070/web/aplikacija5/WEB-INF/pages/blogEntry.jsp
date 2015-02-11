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

.opisKomentari {
	text-align: left;
	font-family: sans-serif;
	font-style: italic;
	font-size: 0.8em;
}

.opisEntry {
	text-align: left;
	font-family: sans-serif;
	font-style: italic;
	font-size: 0.7em;
}

.entryText {
	text-align: left;
	font-family: sans-serif;
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
	<c:choose>
		<c:when test="${empty sessionScope['current.user.id']}">
			<div class="topcorner">
				<p>Korisnik nije prijavljen.</p>
			</div>
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
	<a href="${pageContext.servletContext.contextPath}/servleti/main">Povratak
		na početnu stranicu</a>
	<c:choose>
		<c:when
			test="${sessionScope['current.user.nick'] == entry.creator.nick}">
			<br>
			<a
				href="${pageContext.servletContext.contextPath}/servleti/edit?nick=${entry.creator.nick}&id=${entry.id}">Uredi
				zapis bloga</a>
		</c:when>
	</c:choose>
	<h2>${entry.title}</h2>
	<div class="entryText">
		<h3>${entry.text}</h3>
	</div>
	<div class="opisEntry">
		<h4>Datum kreiranja zapisa: ${entry.createdAt}, datum zadnje
			modifikacije zapisa: ${entry.lastModifiedAt}, kreirao korisnik:
			${entry.creator.nick}.</h4>
	</div>
	Komentari:
	<table>
		<c:forEach var="c" items="${comments}">
			<tr>
				<td><div class="opisKomentari">${c.usersEMail},
						${c.postedOn}</div></td>
				<td>${c.message}</td>
			</tr>
		</c:forEach>
	</table>
	<form action="${pageContext.request.contextPath}/servleti/saveComment"
		method="post">
		<table>
			<tr>
				<td><div class="opis">Novi komentar:</div></td>
				<td><textarea name="message" rows="5" cols="120">${comment.message}</textarea><br>
					<c:if test="${comment.hasErrors('message')}">
						<div class="greska">
							<c:out value="${comment.getError('message')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">E-mail</div></td>
				<td><input type="text" name="email"
					value='<c:out value="${comment.email}"/>' size="50"><br>
					<c:if test="${comment.hasErrors('email')}">
						<div class="greska">
							<c:out value="${comment.getError('email')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><input type="submit" name="type" value="Komentiraj"></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${entry.id}">
	</form>
</body>
</html>

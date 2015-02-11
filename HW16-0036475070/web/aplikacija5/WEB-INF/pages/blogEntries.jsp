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
</style>
</head>

<body>
	<h1>Lista registriranih autora</h1>
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
		<c:when test="${blogs.isEmpty()}">
			<p>Trenutno nema evidentiranih zapisa na blogu autora: ${nick}.</p>
		</c:when>
		<c:otherwise>
			<p>Lista zapisa na blogu autora: ${nick}</p>
			<ul>
				<c:forEach var="b" items="${blogs}">
					<li><a
						href="${pageContext.servletContext.contextPath}/servleti/author/${nick}/${b.id}">${b.title},
							${b.createdAt}</a></li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${sessionScope['current.user.nick'] == nick}">
			<a
				href="${pageContext.servletContext.contextPath}/servleti/author/${nick}/new">Novi
				zapis bloga</a>
		</c:when>
	</c:choose>
</body>
</html>

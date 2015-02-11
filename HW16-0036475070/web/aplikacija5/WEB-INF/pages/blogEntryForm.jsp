<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Zapis bloga</title>


<style type="text/css">
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
	<c:choose>
		<c:when test="${empty entry.id}">
			<h1>Novi zapis bloga.</h1>
		</c:when>
		<c:otherwise>
			<h1>Uređivanje zapisa bloga.</h1>
		</c:otherwise>
	</c:choose>
	<form
		action="${pageContext.request.contextPath}/servleti/saveBlogEntry"
		method="post">
		<table>
			<tr>
				<td><div class="opis">Naslov</div></td>
				<td><input type="text" name="title"
					value='<c:out value="${entry.title}"/>' size="50"><br>
					<c:if test="${entry.hasErrors('title')}">
						<div class="greska">
							<c:out value="${entry.getError('title')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Tekst</div></td>
				<td><textarea name="text" rows="25" cols="120">${entry.text}</textarea><br>
					<c:if test="${entry.hasErrors('text')}">
						<div class="greska">
							<c:out value="${entry.getError('text')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="type" value="Pohrani"> <input
					type="submit" name="type" value="Odustani"></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${entry.id}">
		<input type="hidden" name="nick" value="${nick}">
	</form>
</body>
</html>
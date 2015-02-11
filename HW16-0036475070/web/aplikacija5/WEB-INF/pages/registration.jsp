<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Registracija novih korisnika</title>


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
	<h1>Unesite informacije potrebne za uspješnu registraciju.</h1>
	<form action="${pageContext.request.contextPath}/servleti/saveUser"
		method="post">
		<table>
			<tr>
				<td><div class="opis">Ime</div></td>
				<td><input type="text" name="firstName"
					value='<c:out value="${user.firstName}"/>' size="30"><br>
					<c:if test="${user.hasErrors('firstName')}">
						<div class="greska">
							<c:out value="${user.getError('firstName')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Prezime</div></td>
				<td><input type="text" name="lastName"
					value='<c:out value="${user.lastName}"/>' size="40"><br>
					<c:if test="${user.hasErrors('lastName')}">
						<div class="greska">
							<c:out value="${user.getError('lastName')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">E-Mail</div></td>
				<td><input type="text" name="email"
					value='<c:out value="${user.email}"/>' size="50"><br>
					<c:if test="${user.hasErrors('email')}">
						<div class="greska">
							<c:out value="${user.getError('email')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Korisničko ime</div></td>
				<td><input type="text" name="nickname"
					value='<c:out value="${user.nickname}"/>' size="30"><br>
					<c:if test="${user.hasErrors('nickname')}">
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
				<td><input type="submit" name="type" value="Registriraj">
					<input type="submit" name="type" value="Odustani"></td>
			</tr>
		</table>
	</form>
</body>
</html>
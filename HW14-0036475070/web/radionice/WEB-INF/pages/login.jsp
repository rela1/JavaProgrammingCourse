<%@ page import="java.lang.String"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String greska = (String) request.getAttribute("greska");
%>

<html>
<head>
<title>Prijava</title>

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
</style>
</head>

<body>
	<h1>Prijava korisnika</h1>
	<form action="login" method="post">
		<%
			if (greska != null) {
		%>
		<div class="greska">
			<c:out value="${greska}"></c:out>
		</div>
		<%
			}
		%>
		<table>
			<tr>
				<td><div class="opis">Korisničko ime</div></td>
				<td><input type="text" name="username" size="50"><br></td>
			</tr>
			<tr>
				<td><div class="opis">Lozinka</div></td>
				<td><input type="password" name="password" size="50"><br>
				</td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="metoda" value="Prijavi">
					<input type="submit" name="metoda" value="Odustani"></td>
			</tr>
		</table>
	</form>
</body>
</html>
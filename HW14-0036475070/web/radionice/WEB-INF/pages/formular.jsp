<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Radionica</title>


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
	<h1>
		<c:choose>
			<c:when test="${radionica.id.isEmpty()}">
			Nova radionica
			</c:when>
			<c:otherwise>
			Uređivanje radionice
			</c:otherwise>
		</c:choose>
	</h1>
	<form action="save" method="post">
		<table>
			<tr>
				<td><div class="opis">Naziv</div></td>
				<td><input type="text" name="naziv"
					value='<c:out value="${radionica.naziv}"/>' size="50"><br>
					<c:if test="${radionica.imaPogresku('naziv')}">
						<div class="greska">
							<c:out value="${radionica.dohvatiPogresku('naziv')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Datum</div></td>
				<td><input type="text" name="datum"
					value='<c:out value="${radionica.datum}"/>' size="20"><br>
					<c:if test="${radionica.imaPogresku('datum')}">
						<div class="greska">
							<c:out value="${radionica.dohvatiPogresku('datum')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Oprema</div></td>
				<td><select size="${oprema.size()}" name="oprema" multiple>
						<c:forEach var="opremaEntry" items="${oprema}">
							<c:choose>
								<c:when
									test="${radionica.oprema.contains(opremaEntry.value.id)}">
									<option value="${opremaEntry.value.id}" selected>${opremaEntry.value.vrijednost}
								</c:when>
								<c:otherwise>
									<option value="${opremaEntry.value.id}">${opremaEntry.value.vrijednost}
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td><div class="opis">Trajanje</div></td>
				<td><select size="1" name="trajanje">
						<c:forEach var="trajanjeEntry" items="${trajanje}">
							<c:choose>
								<c:when
									test="${radionica.trajanje.equals(trajanjeEntry.value.id)}">
									<option value="${trajanjeEntry.value.id}" selected>${trajanjeEntry.value.vrijednost}
								</c:when>
								<c:otherwise>
									<option value="${trajanjeEntry.value.id}">${trajanjeEntry.value.vrijednost}
								</c:otherwise>
							</c:choose>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td><div class="opis">Publika</div></td>
				<td><c:forEach var="publikaEntry" items="${publika}">
						<c:choose>
							<c:when
								test="${radionica.publika.contains(publikaEntry.value.id)}">
								<input type="checkbox" name="publika"
									value="${publikaEntry.value.id}" checked>${publikaEntry.value.vrijednost}<br>
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="publika"
									value="${publikaEntry.value.id}">${publikaEntry.value.vrijednost}<br>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${radionica.imaPogresku('publika')}">
						<div class="greska">
							<c:out value="${radionica.dohvatiPogresku('publika')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Maksimalno polaznika</div></td>
				<td><input type="text" name="maksPolaznika"
					value='<c:out value="${radionica.maksPolaznika}"/>' size="5"><br>
					<c:if test="${radionica.imaPogresku('maksPolaznika')}">
						<div class="greska">
							<c:out value="${radionica.dohvatiPogresku('maksPolaznika')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">E-mail</div></td>
				<td><input type="text" name="email"
					value='<c:out value="${radionica.email}"/>' size="50"><br>
					<c:if test="${radionica.imaPogresku('email')}">
						<div class="greska">
							<c:out value="${radionica.dohvatiPogresku('email')}"></c:out>
						</div>
					</c:if></td>
			</tr>
			<tr>
				<td><div class="opis">Dopuna</div></td>
				<td><textarea name="dopuna" rows="5" cols="120">${radionica.dopuna}</textarea><br></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" name="metoda" value="Pohrani">
					<input type="submit" name="metoda" value="Odustani"></td>
			</tr>
		</table>
		<input type="hidden" name="id" value="${radionica.id}">
	</form>
</body>
</html>
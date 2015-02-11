<%@ page import="java.lang.String"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
	<h1>Popis dostupnih anketa: </h1>
	<p>Izaberite anketu u kojoj želite sudjelovati.</p>
	<ol>
		<c:forEach var="p" items="${polls}">
			<li><a href="glasanje?pollID=${p.ID}">${p.message}</a></li>
		</c:forEach>
	</ol>
</body>
</html>
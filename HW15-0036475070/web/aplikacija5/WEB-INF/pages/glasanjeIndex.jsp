<%@ page import="java.lang.String"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<body>
	<h1>${poll.title}</h1>
	<p>${poll.message}</p>
	<a href="/aplikacija5/index.html">Index</a>
	<ol>
		<c:forEach var="p" items="${pollEntries}">
			<li><a href="glasanje-glasaj?pollID=${poll.ID}&id=${p.ID}">${p.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>
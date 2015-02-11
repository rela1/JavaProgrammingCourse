<%@ page import="java.lang.System"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
	String bgColor = (String) request.getSession().getAttribute(
			"pickedBgCol");

	if (bgColor == null)
		bgColor = "WHITE";
%>

<html>
<body BGCOLOR="<%=bgColor%>">
	<h1>Vrijeme života web-aplikacije</h1>
	<p>
		Web-aplikacija je pokrenuta:
		<%
		long timeCreated = ((Long) pageContext.getServletContext()
				.getAttribute("time")).longValue();
		long timeRunning = System.currentTimeMillis() - timeCreated;
		long days = timeRunning / 86400000;
		timeRunning -= days * 86400000;
		long hours = timeRunning / 3600000;
		timeRunning -= hours * 3600000;
		long minutes = timeRunning / 60000;
		timeRunning -= minutes * 60000;
		long seconds = timeRunning / 1000;
		timeRunning -= seconds * 1000;
	%>
		<%=days%>
		dana,
		<%=hours%>
		sati,
		<%=minutes%>
		minuta,
		<%=seconds%>
		sekundi and
		<%=timeRunning%>
		milisekundi. <br> <a href="index.jsp">Index</a>
</body>
</html>
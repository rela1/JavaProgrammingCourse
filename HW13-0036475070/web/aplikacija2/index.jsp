<%@ page import="java.lang.String"
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
	<a href="colors.jsp">Odabir boje pozadine</a>
	<br>
	<a href="squares?a=100&b=120">Kvadrati brojeva od 100 do 120</a>
	<br>
	<a href="stories/funny.jsp">Smiješne priče</a>
	<br>
	<a href="report.jsp">3D tortni prikaz korištenja različitih OS-ova</a>
	<br>
	<a href="powers?a=1&b=100&n=3">Excel datoteka s brojevima i
		njihovim potencijama</a>
	<br>
	<a href="appinfo.jsp">Vrijeme života web-aplikacije</a>
	<br>
	<a href="glasanje">Glasajte za svojeg omiljenog izvođača</a>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Historial</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Estado de mis postulaciones</h2>
    <table>
        <tr><th>Postulante</th><th>Oferta</th><th>Estado final</th><th>Aprobado</th></tr>
        <c:forEach var="p" items="${historial}">
            <tr><td>${p.nombre}</td><td>${p.ofertaTitulo}</td><td>${p.estado}</td><td>${p.aprobado}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

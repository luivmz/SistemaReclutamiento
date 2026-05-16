<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Historial</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header><h1>Talento Academico</h1><nav><a href="/postulante/mis-postulaciones">Mis postulaciones</a><a href="/logout">Salir</a></nav></header>
<main>
    <h2>Historial de procesos</h2>
    <table>
        <tr><th>Postulante</th><th>Oferta</th><th>Estado final</th><th>Puntaje</th></tr>
        <c:forEach var="p" items="${historial}">
            <tr><td>${p.nombre}</td><td>${p.ofertaTitulo}</td><td>${p.estado}</td><td>${p.puntaje}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

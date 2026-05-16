<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Mis postulaciones</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header>
    <h1>Talento Academico</h1>
    <nav><a href="/ofertas">Ofertas</a><a href="/postulante/evaluacion">Evaluacion</a><a href="/postulante/historial">Historial</a><a href="/logout">Salir</a></nav>
</header>
<main>
    <h2>Mis postulaciones</h2>
    <table>
        <tr><th>Oferta</th><th>Estado</th><th>Puntaje</th><th>Aprobado</th></tr>
        <c:forEach var="p" items="${postulaciones}">
            <tr>
                <td>${p.ofertaTitulo}</td>
                <td>${p.estado}</td>
                <td>${p.puntaje}</td>
                <td>${p.aprobado}</td>
            </tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

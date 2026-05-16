<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Evaluacion</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header><h1>Talento Academico</h1><nav><a href="/postulante/mis-postulaciones">Mis postulaciones</a><a href="/logout">Salir</a></nav></header>
<main>
    <h2>Evaluacion academica</h2>
    <section class="card">
        <p>Esta seccion queda preparada para la evaluacion por preguntas. En la siguiente etapa se conectara con pruebas y calificacion.</p>
    </section>
    <h2>Ofertas disponibles</h2>
    <table>
        <tr><th>Oferta</th><th>Area</th></tr>
        <c:forEach var="oferta" items="${ofertas}">
            <tr><td>${oferta.titulo}</td><td>${oferta.areaNombre}</td></tr>
        </c:forEach>
    </table>
</main>
</body>
</html>

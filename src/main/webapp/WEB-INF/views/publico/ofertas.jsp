<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Ofertas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<header><h1>Talento Academico</h1><nav><a href="/">Inicio</a><a href="/login">Login</a></nav></header>
<main>
    <h2>Ofertas laborales</h2>
    <c:forEach var="oferta" items="${ofertas}">
        <section class="card">
            <h3>${oferta.titulo}</h3>
            <p>${oferta.descripcion}</p>
            <p>Vacantes: ${oferta.vacantes} | Area: ${oferta.areaNombre}</p>
            <a class="btn" href="/ofertas/${oferta.id}">Postular</a>
        </section>
    </c:forEach>
</main>
</body>
</html>

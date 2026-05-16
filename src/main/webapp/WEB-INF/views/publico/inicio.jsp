<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Talento Academico</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Sistema simple de reclutamiento</h2>
    <p>Proyecto academico para gestionar ofertas, postulantes, evaluaciones y entrevistas.</p>
    <h2>Ofertas recientes</h2>
    <div class="grid">
        <c:forEach var="oferta" items="${ofertas}">
            <section class="card">
                <h3>${oferta.titulo}</h3>
                <p>${oferta.descripcion}</p>
                <p><strong>Area:</strong> ${oferta.areaNombre}</p>
                <a class="btn" href="/ofertas/${oferta.id}">Ver detalle</a>
            </section>
        </c:forEach>
    </div>
</main>
</body>
</html>

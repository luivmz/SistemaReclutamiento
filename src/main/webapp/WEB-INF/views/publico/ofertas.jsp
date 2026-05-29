<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Ofertas Docentes</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Ofertas docentes y administrativas</h2>
    <c:forEach var="oferta" items="${ofertas}">
        <section class="card">
            <h3>${oferta.titulo}</h3>
            <p>${oferta.descripcion}</p>
            <p>Vacantes: ${oferta.vacantes} | Area academica: ${oferta.areaNombre}</p>
            <a class="btn" href="/ofertas/${oferta.id}">Postular</a>
        </section>
    </c:forEach>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

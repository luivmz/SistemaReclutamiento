<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Colegio Andino - Sistema de Reclutamiento Docente</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Bienvenido al Sistema de Reclutamiento del Colegio Andino</h2>
    <section class="card">
        <h3>Colegio Andino - Huancayo</h3>
        <p>Institucion educativa con mas de 110 años formando estudiantes con excelencia academica y valores.</p>
        <p>El Colegio Andino busca incorporar profesionales comprometidos con la educacion, la formacion integral y el desarrollo de nuestros estudiantes.</p>
        <a class="btn" href="/ofertas">Ver Ofertas</a>
        <a class="btn" href="/login">Iniciar Sesion</a>
        <a class="btn secundario" href="/registro">Registrarse</a>
    </section>
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
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

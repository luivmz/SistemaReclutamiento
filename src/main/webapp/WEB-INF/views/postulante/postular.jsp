<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Postular</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Postular a ${oferta.titulo}</h2>
    <p class="error">${error}</p>
    <form method="post" action="/ofertas/${oferta.id}/postular" class="card">
        <label>Nombre</label><input name="nombre" value="${empty postulante.nombre ? sessionScope.nombre : postulante.nombre}" minlength="2" maxlength="120" required>
        <label>Email</label><input type="email" name="email" value="${empty postulante.email ? sessionScope.email : postulante.email}" maxlength="120" required>
        <label>Telefono</label><input name="telefono" value="${empty postulante.telefono ? sessionScope.telefono : postulante.telefono}" maxlength="40" required>
        <label>Experiencia educativa</label><textarea name="experiencia" rows="4" maxlength="700">${postulante.experiencia}</textarea>
        <label>Habilidades pedagogicas</label><textarea name="habilidades" rows="4" maxlength="700">${postulante.habilidades}</textarea>
        <label>CV como enlace de Drive</label><input name="cv" value="${postulante.cv}" maxlength="255" placeholder="https://drive.google.com/...">
        <button type="submit">Enviar postulacion</button>
    </form>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

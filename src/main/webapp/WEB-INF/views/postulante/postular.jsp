<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Postular</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Postular a ${oferta.titulo}</h2>
    <p class="error">${error}</p>
    <form method="post" action="/ofertas/${oferta.id}/postular" class="card">
        <label>Nombre</label><input name="nombre" value="${sessionScope.nombre}" required>
        <label>Email</label><input type="email" name="email" value="${sessionScope.email}" required>
        <label>Telefono</label><input name="telefono" value="${sessionScope.telefono}" required>
        <label>Experiencia educativa</label><textarea name="experiencia" rows="4"></textarea>
        <label>Habilidades pedagogicas</label><textarea name="habilidades" rows="4"></textarea>
        <label>CV opcional</label><input name="cv" placeholder="Nombre del archivo o enlace">
        <button type="submit">Enviar postulacion</button>
    </form>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

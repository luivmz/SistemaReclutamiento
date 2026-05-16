<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Contacto</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Contacto</h2>
    <section class="card">
        <p>Email: informes@talentoacademico.edu</p>
        <p>Horario: lunes a viernes de 9:00 a 18:00</p>
    </section>
    <form method="post" action="/contacto" class="card">
        <h3>Envianos tu consulta</h3>
        <p class="success">${mensaje}</p>
        <label>Nombre</label>
        <input name="nombre" required>
        <label>Email</label>
        <input type="email" name="email" required>
        <label>Asunto</label>
        <input name="asunto" required>
        <label>Mensaje</label>
        <textarea name="mensaje" rows="5" required></textarea>
        <button type="submit">Enviar</button>
    </form>
</main>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head><title>Contacto Colegio Andino</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Contacto</h2>
    <section class="card">
        <p>Si desea formar parte de nuestra institucion educativa, comuniquese con nosotros o revise nuestras convocatorias disponibles.</p>
        <p><strong>Direccion:</strong> Jr. Guido 512 - Huancayo</p>
        <p><strong>Telefono:</strong> 064 232521</p>
        <p><strong>Celular:</strong> 944 688 081</p>
        <p><strong>Correo:</strong> secretaria@andino.edu.pe</p>
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
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

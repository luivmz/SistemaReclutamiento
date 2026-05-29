<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Iniciar sesion</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<main>
    <h2>Iniciar sesion</h2>
    <p>Sistema de Gestion de Postulantes del Colegio Andino.</p>
    <p class="success">${mensaje}</p>
    <p class="error">${error}</p>
    <form method="post" action="/login" class="card">
        <label>Email</label>
        <input type="email" name="email" required>
        <label>Password</label>
        <input type="password" name="password" required>
        <button type="submit">Ingresar</button>
    </form>
    <p>Demo admin: coordinador@colegio.edu.pe / 123456</p>
    <p>Demo postulante: docente.matematica@demo.com / 123456</p>
    <p><a href="/registro">Crear cuenta de postulante</a></p>
</main>
<%@ include file="../includes/footer.jsp" %>
</body>
</html>

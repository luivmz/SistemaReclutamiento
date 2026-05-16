<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="/resources/css/style.css">
</head>
<body>
<header><h1>Talento Academico</h1><nav><a href="/">Inicio</a><a href="/registro">Registro</a></nav></header>
<main>
    <h2>Iniciar sesion</h2>
    <p class="error">${error}</p>
    <form method="post" action="/login" class="card">
        <label>Email</label>
        <input type="email" name="email" required>
        <label>Password</label>
        <input type="password" name="password" required>
        <button type="submit">Ingresar</button>
    </form>
    <p>Demo admin: admin@talento.edu / 123456</p>
</main>
</body>
</html>

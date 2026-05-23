<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Postulantes</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Registrar postulante</h2>
    <form method="post" action="/admin/postulantes" class="card">
        <input type="hidden" name="id" value="${postulante.id}">
        <p class="error">${error}</p>
        <label>Usuario existente</label>
        <select name="usuarioId" required>
            <c:forEach var="u" items="${usuarios}">
                <option value="${u.id}">${u.nombre} - ${u.email}</option>
            </c:forEach>
        </select>
        <label>Oferta laboral</label>
        <select name="ofertaId" required>
            <c:forEach var="o" items="${ofertas}">
                <option value="${o.id}">${o.titulo}</option>
            </c:forEach>
        </select>
        <label>Nombre</label><input name="nombre" value="${postulante.nombre}">
        <label>Email</label><input name="email" value="${postulante.email}">
        <label>Telefono</label><input name="telefono" value="${postulante.telefono}">
        <label>Experiencia</label><textarea name="experiencia" rows="3">${postulante.experiencia}</textarea>
        <label>Habilidades</label><textarea name="habilidades" rows="3">${postulante.habilidades}</textarea>
        <label>CV opcional</label><input name="cv" value="${postulante.cv}" placeholder="Nombre del archivo o enlace">
        <label>Estado</label>
        <select name="estado">
            <option value="POSTULADO">POSTULADO</option>
            <option value="EN_ENTREVISTA">EN_ENTREVISTA</option>
            <option value="APROBADO">APROBADO</option>
            <option value="RECHAZADO">RECHAZADO</option>
        </select>
        <label>Observacion</label><textarea name="observacion" rows="3">${postulante.observacion}</textarea>
        <button type="submit">Guardar postulacion</button>
        <a class="btn secundario" href="/admin/postulantes">Nuevo</a>
    </form>

    <h2 class="page-title">Postulantes activos</h2>
    <table>
        <tr><th>Nombre</th><th>Email</th><th>Oferta</th><th>Estado</th><th>Acciones</th></tr>
        <c:forEach var="p" items="${activos}">
            <tr>
                <td>${p.nombre}</td><td>${p.email}</td><td>${p.ofertaTitulo}</td><td>${p.estado}</td>
                <td>
                    <a class="btn" href="/admin/postulantes/editar/${p.id}">Editar</a>
                    <a class="btn secundario" href="/admin/postulantes/estado/${p.id}/EN_ENTREVISTA">En entrevista</a>
                    <a class="btn" href="/admin/postulantes/aprobar/${p.id}">Aprobar</a>
                    <a class="btn secundario" href="/admin/postulantes/rechazar/${p.id}">Rechazar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
    <h2>Historial</h2>
    <table>
        <tr><th>Nombre</th><th>Oferta</th><th>Estado</th><th>Aprobado</th><th>Acciones</th></tr>
        <c:forEach var="p" items="${historial}">
            <tr>
                <td>${p.nombre}</td><td>${p.ofertaTitulo}</td><td>${p.estado}</td><td>${p.aprobado}</td>
                <td>
                    <a class="btn" href="/admin/postulantes/editar/${p.id}">Editar</a>
                    <a class="btn secundario" href="/admin/postulantes/estado/${p.id}/POSTULADO">Reabrir</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

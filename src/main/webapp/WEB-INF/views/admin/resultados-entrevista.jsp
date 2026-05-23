<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Resultados de entrevistas</title><link rel="stylesheet" href="/resources/css/style.css"></head>
<body>
<%@ include file="../includes/public-header.jsp" %>
<div class="admin-layout">
    <%@ include file="../includes/admin-sidebar.jsp" %>
<main class="admin-content">
    <h2 class="page-title">Resultados de entrevistas</h2>
    <p>Para registrar un nuevo resultado, hazlo desde <a href="/admin/entrevistas">Entrevistas</a>.</p>
    <table>
        <tr>
            <th>Postulante</th>
            <th>Tipo</th>
            <th>Fecha entrevista</th>
            <th>Resultado</th>
            <th>Puntaje</th>
            <th>Registrado por</th>
            <th>Fecha registro</th>
            <th>Acciones</th>
        </tr>
        <c:forEach var="r" items="${resultados}">
            <tr>
                <td>${r.entrevista.postulante.nombre}</td>
                <td>${r.entrevista.tipoEntrevista}</td>
                <td>${r.entrevista.fecha}</td>
                <td>${r.resultado}</td>
                <td>${r.puntaje}</td>
                <td>${r.registradoPor}</td>
                <td>${r.fechaRegistro}</td>
                <td>
                    <a class="btn" href="/admin/resultados-entrevista/editar/${r.id}">Editar</a>
                    <a class="btn secundario" href="/admin/resultados-entrevista/eliminar/${r.id}">Eliminar</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</main>
</div>
</body>
</html>

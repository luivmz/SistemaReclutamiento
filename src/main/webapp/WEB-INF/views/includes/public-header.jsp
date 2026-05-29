<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <div class="header-inner">
        <h1><a href="/">Colegio Andino</a></h1>
        <nav class="top-nav" aria-label="Navegacion principal">
            <a href="/">Inicio</a>
            <a href="/ofertas">Ofertas Docentes</a>
            <a href="/areas">Areas Academicas</a>
            <a href="/publicidad">Publicidad</a>
            <c:choose>
                <c:when test="${sessionScope.rol == 'ADMIN'}">
                    <a href="/admin/dashboard">Dashboard</a>
                </c:when>
                <c:when test="${sessionScope.rol == 'POSTULANTE'}">
                    <a href="/postulante/dashboard">Dashboard</a>
                </c:when>
                <c:otherwise>
                    <a href="/login">Login</a>
                </c:otherwise>
            </c:choose>
            <a href="/contacto">Contacto</a>
        </nav>
    </div>
</header>

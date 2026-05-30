# MODELO DE NEGOCIO - Sistema de Reclutamiento
## Colegio Andino de Huancayo

---

## 📊 BUSINESS MODEL CANVAS

```
┌─────────────────────┬──────────────────────────────────────┬──────────────────────┐
│  ALIADOS CLAVE      │   PROPUESTA DE VALOR                 │  SEGMENTOS CLIENTES  │
├─────────────────────┼──────────────────────────────────────┼──────────────────────┤
│ • LinkedIn          │ ✓ Automatizar reclutamiento         │ • Administrativos     │
│ • Plataformas eval  │ ✓ Evaluar competencias objetivo      │ • Evaluadores/Jefes   │
│ • Cloud Storage     │ ✓ Reducir tiempo en 40%              │ • Candidatos          │
│                     │ ✓ Criterios estandarizados           │                       │
│                     │ ✓ Reportes centralizados             │                       │
├─────────────────────┼──────────────────────────────────────┼──────────────────────┤
│ ACTIVIDADES CLAVE   │        RECURSOS CLAVE                │      CANALES          │
├─────────────────────┼──────────────────────────────────────┼──────────────────────┤
│ • Gestión ofertas   │ • Base de datos centralizada         │ • Web browser         │
│ • Evaluación tests  │ • Infraestructura web (Spring Boot)  │ • Email               │
│ • Seguimiento       │ • Equipo desarrollo                  │ • Dashboard           │
│ • Entrevistas       │ • Almacenamiento (H2/PostreSQL)      │ • Reportes            │
├─────────────────────┼──────────────────────────────────────┼──────────────────────┤
│ ESTRUCTURA COSTOS   │        FLUJOS DE INGRESOS            │                       │
├─────────────────────┼──────────────────────────────────────┤                       │
│ • Desarrollo        │ ✓ Beneficio: Reducción de tiempo     │                       │
│ • Infraestructura   │ ✓ Beneficio: Mejor selección         │                       │
│ • Mantenimiento     │ ✓ Beneficio: Trazabilidad            │                       │
└─────────────────────┴──────────────────────────────────────┴──────────────────────┘
```

---

## 🎯 OBJETIVOS ESTRATÉGICOS

| Objetivo | Descripción | Métrica |
|----------|-------------|---------|
| **Automatizar procesos** | Centralizar reclutamiento vs LinkedIn | Reducir tiempo en 40% |
| **Estandarizar evaluación** | Criterios objetivos y replicables | Consistencia 95%+ |
| **Mejorar experiencia** | Candidatos y evaluadores | Satisfacción >4.5/5 |
| **Trazabilidad** | Registrar cada paso del proceso | 100% auditable |
| **Reportes analíticos** | Insights de procesos de selección | Reportes mensuales |

---

## 📈 FLUJO DE RECLUTAMIENTO

```
┌─────────────────┐
│  1. CREAR OFERTA │  Admin crea oferta laboral con detalles y preguntas
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 2. POSTULAR     │  Candidatos ven ofertas activas y se postulan
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 3. EVALUAR      │  Sistema automático: preguntas de selección online
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 4. ENTREVISTAR  │  Evaluador entrevista a candidatos con mejor puntaje
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│ 5. DECIDIR      │  Seleccionar candidato final o rechazar
└────────┬────────┘
         │
         ▼
    ┌─────────┐
    │ APROBADO │ o RECHAZADO
    └─────────┘
```

---

## 👥 ACTORES DEL SISTEMA

### 🔑 ADMINISTRADOR (RRHH)
**Responsabilidades:**
- Crear y gestionar ofertas laborales
- Definir preguntas y criterios de evaluación
- Asignar evaluadores
- Monitorear todo el proceso

**Acceso a:**
- Dashboard completo
- Todos los reportes
- Gestión de usuarios y permisos
- Configuración del sistema

---

### 🔍 EVALUADOR
**Responsabilidades:**
- Revisar postulaciones
- Calificar evaluaciones automáticas
- Conducir entrevistas
- Documentar observaciones

**Acceso a:**
- Listado de postulantes por oferta
- Detalles del candidato (CV, puntaje)
- Formulario de entrevista
- Historial de evaluaciones

---

### 📝 CANDIDATO/POSTULANTE
**Responsabilidades:**
- Postularse a ofertas
- Completar evaluaciones online
- Participar en entrevistas
- Seguimiento de su aplicación

**Acceso a:**
- Ofertas disponibles
- Estado de su postulación
- Resultados de evaluaciones
- Historial de postulaciones

---

## 🗄️ ARQUITECTURA DE DATOS

### Entidades Principales

```
USUARIOS (6 registros)
├─ id, nombre, email, password, telefono, rol, activo
│
├─→ POSTULANTES (1:N)
│   ├─ id, nombre, email, experiencia, habilidades
│   ├─ estado, puntaje, aprobado, fecha_postulacion
│   │
│   ├─→ ENTREVISTAS (1:N)
│   │   └─ tipo, fecha, hora, lugar, resultado
│   │
│   └─→ OFERTAS_LABORALES (M:1)
│       └─ titulo, descripcion, vacantes, activa
│
└─→ ÁREAS (1:N)
    └─ nombre, descripcion

PREGUNTAS (M:N) ↔ OFERTAS_LABORALES
├─ enunciado
└─ opcion_correcta
```

### Relaciones Clave
- **USUARIOS → POSTULANTES (1:N)**: Un usuario puede tener múltiples postulaciones
- **ÁREAS → OFERTAS (1:N)**: Un área tiene múltiples ofertas
- **OFERTAS → POSTULANTES (1:N)**: Una oferta recibe múltiples aplicaciones
- **OFERTAS ↔ PREGUNTAS (M:N)**: Cada oferta tiene múltiples preguntas
- **POSTULANTES → ENTREVISTAS (1:N)**: Un postulante puede tener múltiples entrevistas

---

## 📊 KPIs Y MÉTRICAS

### Métricas de Negocio

| KPI | Fórmula | Target |
|-----|---------|--------|
| **Tiempo promedio de reclutamiento** | (Fecha fin - Fecha inicio) / # ofertas | < 30 días |
| **Tasa de aprobación** | (Aprobados / Total postulantes) × 100 | 5-15% |
| **Tasa de conversión por etapa** | (Pasaron etapa / Total inicio etapa) × 100 | > 40% |
| **Costo por contratación** | Costo total / # contrataciones | Reducir 50% |
| **Satisfacción del candidato** | Encuesta Post-proceso | > 4.0/5.0 |

### Métricas Técnicas

- **Disponibilidad**: 99.9% uptime
- **Tiempo de respuesta**: < 2 segundos promedio
- **Tasa de error**: < 0.1%
- **Usuarios concurrentes**: Soportar 100+ simultáneamente

---

## 💰 MODELO FINANCIERO

### INVERSIÓN INICIAL
- Desarrollo software: $5,000 - $10,000
- Infraestructura (servidor, BD): $1,000 - $2,000
- Configuración inicial: $500 - $1,000

### COSTOS OPERACIONALES MENSUALES
- Hosting/Infraestructura: $100 - $200
- Mantenimiento y soporte: $300 - $500
- Actualizaciones: $100 - $200

### RETORNO DE INVERSIÓN (ROI)
- **Ahorro en horas RRHH**: 40 horas/mes = ~$1,600/mes
- **Mejora en calidad contrataciones**: Mejor productividad = +$5,000/año
- **ROI estimado**: 6-8 meses

---

## ⚠️ RIESGOS Y MITIGACIONES

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|-----------|
| Resistencia al cambio | MEDIA | ALTO | Capacitación extensiva |
| Pérdida de datos | BAJA | CRÍTICO | Backup diarios, redundancia |
| Inseguridad de datos | MEDIA | ALTO | Encriptación, RBAC, auditoría |
| Bajo adoption users | MEDIA | MEDIO | UI intuitivo, soporte constante |
| Escalabilidad insuficiente | BAJA | MEDIO | Arquitectura escalable, caché |

---

## 🚀 ROADMAP DE IMPLEMENTACIÓN

### **FASE 1** (Semanas 1-2) - MVP
- ✅ Sistema de postulación básico
- ✅ Evaluación automática (tests)
- ✅ Gestión de entrevistas
- ✅ Dashboard simple

### **FASE 2** (Semanas 3-4) - Mejoras
- 📋 Notificaciones por email
- 📊 Reportes analíticos
- 🔒 Seguridad mejorada (BCrypt, 2FA)
- 📁 Almacenamiento de CVs

### **FASE 3** (Semanas 5-8) - Optimización
- ⚡ Caché y performance
- 🔎 Búsqueda avanzada
- 📱 Versión mobile
- 🔗 Integración LinkedIn

### **FASE 4** (Futuro) - Innovación
- 🤖 IA para matching de candidatos
- 📹 Entrevistas virtuales integradas
- 📈 Análisis predictivo
- 🌐 Portal multi-idioma

---

## ✅ VENTAJAS COMPETITIVAS

| Aspecto | LinkedIn | Sistemas Genéricos | **NUESTRO SISTEMA** |
|--------|----------|-------------------|-------------------|
| **Costo** | $$ Oneroso | $ Bajo | ✅ Gratuito (interno) |
| **Customización** | ❌ No | ⚠️ Limitada | ✅ Total |
| **Privacidad datos** | ❌ Externa | ⚠️ Tercero | ✅ Interna |
| **Integración** | ❌ Manual | ⚠️ Parcial | ✅ 100% Integrado |
| **Reportes** | ⚠️ Básicos | ⚠️ Limitados | ✅ Detallados |
| **Automatización** | ❌ Nula | ⚠️ Parcial | ✅ Completa |

---

## 📝 CONCLUSIONES

1. **El sistema centraliza** todo lo que hoy está disperso en LinkedIn
2. **Mejora significativamente** la eficiencia del proceso de RRHH
3. **Proporciona trazabilidad** completa del proceso de selección
4. **Genera datos valioso** para análisis y mejora continua
5. **ROI positivo** en 6-8 meses de implementación

---

**Fecha de análisis**: Mayo 2026  
**Versión**: 1.0  
**Estado**: Listo para implementación

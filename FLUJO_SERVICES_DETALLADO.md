# 📚 FLUJO COMPLETO DE SERVICES - SISTEMA DE RECLUTAMIENTO
## Documentación Detallada Línea por Línea

---

## 🎯 ÍNDICE
1. [Arquitectura General](#arquitectura-general)
2. [Flujo del Caso de Uso: Un Usuario Postula a una Oferta](#flujo-del-caso-de-uso)
3. [Análisis Línea por Línea](#análisis-línea-por-línea)
4. [Direccionamiento de Datos](#direccionamiento-de-datos)
5. [Flujo de Evaluación](#flujo-de-evaluación)

---

## 🏗️ Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENTE (Browser)                        │
│         GET /ofertas/{id}/postular - POST /ofertas           │
└────────────────────────┬────────────────────────────────────┘
                         │
                    HTTP/REST
                         │
┌────────────────────────▼────────────────────────────────────┐
│              CONTROLLER LAYER                                │
│              PostulanteController                           │
│  (Recibe solicitudes, valida sesión, llama servicios)      │
└────────────────────────┬────────────────────────────────────┘
                         │
                   Inyección de
                   Dependencias
                         │
┌────────────────────────▼────────────────────────────────────┐
│              SERVICE LAYER (Lógica de Negocio)              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ PostulanteService         OfertaService               │ │
│  │ EvaluacionService         UsuarioService              │ │
│  └────────────────────────┬───────────────────────────────┘ │
│                           │                                  │
│               Llamadas a Repository                          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│              REPOSITORY LAYER (Acceso a Datos)              │
│  PostulanteRepository    OfertaRepository                   │
│  UsuarioRepository       PreguntaRepository                 │
│                  (JpaRepository<T, ID>)                     │
└────────────────────────┬────────────────────────────────────┘
                         │
                    SQL Queries
                         │
┌────────────────────────▼────────────────────────────────────┐
│                   DATABASE (H2)                              │
│  Tablas: postulantes, ofertas_laborales, usuarios, etc.    │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔄 Flujo del Caso de Uso
### "Un usuario se postula a una oferta de trabajo"

### **PASO 1: Usuario solicita formulario de postulación**

```
GET /ofertas/{id}/postular
    ↓
PostulanteController.formularioPostulacion(id=1, ...)
    ↓
ofertaService.buscar(1)
    ↓
Renderizar formulario HTML
```

### **PASO 2: Usuario completa formulario y envía datos**

```
POST /ofertas/{id}/postular
    ↓
PostulanteController.postular(id=1, PostulanteDTO, session, model)
    ↓
PostulanteService.postular(PostulanteDTO, usuarioId)
    ↓
Registrar en BD + retornar PostulanteDTO
    ↓
Redirigir a mis-postulaciones
```

### **PASO 3: Usuario ve su postulación en el historial**

```
GET /postulante/mis-postulaciones
    ↓
PostulanteController.misPostulaciones(session, model)
    ↓
PostulanteService.listarPorUsuario(usuarioId)
    ↓
Retornar lista de PostulanteDTOs
    ↓
Renderizar vista con postulaciones
```

---

## 🔍 Análisis Línea por Línea

### **ARCHIVO: PostulanteController.java**

#### **Método: formularioPostulacion()**
```java
// LÍNEA 27-35: GET /ofertas/{id}/postular
@GetMapping("/ofertas/{id}/postular")
public String formularioPostulacion(@PathVariable Long id, Model model, HttpSession session) {
    // LÍNEA 29-31: Validación de sesión
    // ¿QUÉ HACE?: Verifica si el usuario es ADMIN
    // ¿POR QUÉ?: ADMINs no pueden postularse, solo gestionar candidatos
    // ¿A DÓNDE VA?: Si es admin, redirige a error de acceso
    if ("ADMIN".equals(session.getAttribute("rol"))) {
        return "redirect:/acceso-denegado";
    }
    
    // LÍNEA 32: LLAMADA A SERVICIO 1 - Buscar oferta
    // ¿QUÉ HACE?: Obtiene los datos de la oferta (título, descripción, vacantes)
    // DIRECCIÓN:
    //   1. ofertaService → OfertaServiceImpl
    //   2. ofertaRepository.findById(id) → SQL: SELECT * FROM ofertas_laborales WHERE id=?
    //   3. OfertaMapper.toDTO() → Convierte Entity a DTO
    model.addAttribute("oferta", ofertaService.buscar(id));
    
    // LÍNEA 33: Crear un objeto DTO vacío
    // ¿QUÉ HACE?: Prepara un DTO para que el usuario llene con sus datos
    model.addAttribute("postulante", new PostulanteDTO());
    
    // LÍNEA 34: Retorna vista HTML
    return "postulante/postular";
    // Archivo: src/main/webapp/postulante/postular.html
}
```

**Diagrama de flujo línea 32:**
```
ofertaService.buscar(1)
    ↓
[OfertaServiceImpl.buscar(1)]
    ↓
ofertaRepository.findById(1)
    ↓
[SQL ejecutado en BD H2]
    SELECT * FROM ofertas_laborales WHERE id = 1
    ↓
Resultado: OfertaLaboral entity {id: 1, titulo: "Dev Java", ...}
    ↓
OfertaMapper.toDTO()
    ↓
Resultado: OfertaDTO {id: 1, titulo: "Dev Java", ...}
    ↓
Retorna a Controller
    ↓
model.addAttribute("oferta", OfertaDTO)
    ↓
HTML recibe la oferta para mostrar en el formulario
```

---

#### **Método: postular()**
```java
// LÍNEA 37-52: POST /ofertas/{id}/postular
@PostMapping("/ofertas/{id}/postular")
public String postular(@PathVariable Long id, @ModelAttribute PostulanteDTO postulante,
                       HttpSession session, Model model) {
    
    // LÍNEA 40: OBTENER USUARIO DE SESIÓN
    Long usuarioId = (Long) session.getAttribute("usuarioId");
    // ¿QUÉ HACE?: Extrae el ID del usuario logueado desde la sesión HTTP
    // RAZÓN: Necesitamos saber quién está postulando
    
    // LÍNEA 41-43: Validación de rol
    if ("ADMIN".equals(session.getAttribute("rol"))) {
        return "redirect:/acceso-denegado";
    }
    
    // LÍNEA 44-48: VALIDACIÓN DE POSTULACIÓN DUPLICADA
    // ¿QUÉ HACE?: Verifica si el usuario ya se postuló a esta oferta
    if (postulanteService.yaPostulo(usuarioId, id)) {
        // DIRECCIÓN DE ESTA VALIDACIÓN:
        //   1. postulanteService.yaPostulo(usuarioId=5, ofertaId=1)
        //   2. [PostulanteServiceImpl.yaPostulo()]
        //   3. postulanteRepository.existsByUsuarioIdAndOfertaId(5, 1)
        //   4. [SQL] SELECT COUNT(*) FROM postulantes 
        //            WHERE usuario_id=5 AND oferta_id=1
        //   5. Si existe → retorna TRUE
        //   6. Si no existe → retorna FALSE
        // Si ya postulo, recarga la vista con mensaje de error
        model.addAttribute("oferta", ofertaService.buscar(id));
        model.addAttribute("error", "Ya postulaste a esta oferta.");
        return "postulante/postular";
    }
    
    // LÍNEA 49: Asignar ID de oferta al DTO
    postulante.setOfertaId(id);
    // ¿QUÉ HACE?: El DTO recibe el ID de la oferta a la que se postula
    
    // LÍNEA 50: LLAMADA A SERVICIO PRINCIPAL - Crear postulante
    postulanteService.postular(postulante, usuarioId);
    // ¡¡¡ ESTE ES EL FLUJO MÁS IMPORTANTE !!!
    // DIRECCIÓN DETALLADA A CONTINUACIÓN:
    
    // LÍNEA 51: Redirigir al historial
    return "redirect:/postulante/mis-postulaciones";
}
```

**Diagrama completo de `postular()` en línea 50:**

```
ENTRADA: PostulanteDTO {nombre, email, telefono, ofertaId, ...}
         usuarioId = 5

    ↓
PostulanteServiceImpl.postular(postulante, 5)
    
    ↓ LÍNEA 60-62: Validación (duplique verificación)
    if (dto.getOfertaId() != null && yaPostulo(usuarioId, dto.getOfertaId())) {
        throw new IllegalStateException("Ya postulaste a esta oferta.");
    }
    // Si ya postulo → lanza excepción → Controller la captura
    
    ↓ LÍNEA 63: Limpiar ID
    dto.setId(null);
    // ¿POR QUÉ?: Asegurar que sea un nuevo registro
    
    ↓ LÍNEA 64: Convertir DTO a Entity
    Postulante postulante = PostulanteMapper.toEntity(dto);
    // DTO → Entity Postulante
    // postulante ahora es un objeto Postulante desvinculado de BD
    
    ↓ LÍNEA 65: Asignar estado inicial
    postulante.setEstado(EstadoPostulante.POSTULADO);
    // POSTULADO = estado inicial, esperando evaluación
    
    ↓ LÍNEA 66: Marcar como no aprobado inicialmente
    postulante.setAprobado(false);
    
    ↓ LÍNEA 67: Asignar oferta al postulante
    asignarOferta(dto, postulante);
    // DIRECCIÓN (línea 149-154):
    //   if (dto.getOfertaId() != null) {
    //       OfertaLaboral oferta = ofertaRepository.findById(dto.getOfertaId())
    //       // [SQL] SELECT * FROM ofertas_laborales WHERE id = 1
    //       postulante.setOferta(oferta);
    //   }
    
    ↓ LÍNEA 68: Obtener usuario de la BD
    Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow();
    // [SQL] SELECT * FROM usuarios WHERE id = 5
    // Si no existe → lanza NoSuchElementException
    
    ↓ LÍNEA 69: Vincular usuario a postulante
    postulante.setUsuario(usuario);
    // Relación ManyToOne: Postulante → Usuario
    
    ↓ LÍNEA 70: Copiar nombre del usuario
    postulante.setNombre(usuario.getNombre());
    // Si el usuario no completa nombre, usa el registrado
    
    ↓ LÍNEA 71: Copiar email del usuario
    postulante.setEmail(usuario.getEmail());
    
    ↓ LÍNEA 72-74: Usar teléfono del usuario si está vacío
    if (postulante.getTelefono() == null || postulante.getTelefono().isBlank()) {
        postulante.setTelefono(usuario.getTelefono());
    }
    
    ↓ LÍNEA 75: GUARDAR EN BASE DE DATOS
    Postulante savedPostulante = postulanteRepository.save(postulante);
    // [SQL] INSERT INTO postulantes 
    //       (nombre, email, telefono, estado, oferta_id, usuario_id, ...)
    //       VALUES ('Juan', 'juan@mail.com', '987654', 'POSTULADO', 1, 5, ...)
    // Auto-genera ID y retorna el Postulante con ID asignado
    
    ↓ LÍNEA 75: Convertir Entity a DTO
    return PostulanteMapper.toDTO(savedPostulante);
    // Postulante entity → PostulanteDTO (con ID generado)
    
    ↓
SALIDA: PostulanteDTO {id: 15, nombre: "Juan", email: "juan@mail.com", 
                       estado: "POSTULADO", ofertaId: 1, usuarioId: 5, ...}
    
    ↓ VUELVE A CONTROLLER (línea 50)
    Resultado guardado, se ejecuta línea 51
    
    ↓
    return "redirect:/postulante/mis-postulaciones";
    
    ↓ BROWSER
    GET /postulante/mis-postulaciones
```

---

#### **Método: misPostulaciones()**
```java
// LÍNEA 54-59
@GetMapping("/postulante/mis-postulaciones")
public String misPostulaciones(HttpSession session, Model model) {
    // LÍNEA 56: Obtener ID del usuario
    Long usuarioId = (Long) session.getAttribute("usuarioId");
    
    // LÍNEA 57: LLAMADA A SERVICIO - Listar postulaciones
    model.addAttribute("postulaciones", postulanteService.listarPorUsuario(usuarioId));
    
    // DIRECCIÓN DE listarPorUsuario():
    //   1. PostulanteServiceImpl.listarPorUsuario(usuarioId=5)
    //   2. postulanteRepository.findByUsuarioId(5)
    //   3. [SQL] SELECT * FROM postulantes WHERE usuario_id = 5
    //   4. Resultados: [Postulante {id:1, oferta:'Dev Java', estado:'POSTULADO'},
    //                   Postulante {id:2, oferta:'Dev Python', estado:'EN_EVALUACION'}]
    //   5. .stream().map(PostulanteMapper::toDTO).toList()
    //      Convierte cada Entity a DTO
    //   6. Retorna: [PostulanteDTO {id:1, ...}, PostulanteDTO {id:2, ...}]
    
    // LÍNEA 58: Renderizar vista
    return "postulante/mis-postulaciones";
}
```

---

### **ARCHIVO: PostulanteServiceImpl.java**

#### **Método: listarActivos()**
```java
// LÍNEA 33-38
public List<PostulanteDTO> listarActivos() {
    return postulanteRepository.findByEstadoIn(List.of(
            EstadoPostulante.POSTULADO,      // Postulantes nuevos
            EstadoPostulante.EN_EVALUACION   // Postulantes siendo evaluados
    ))
    .stream()                              // Convierte lista en stream
    .map(PostulanteMapper::toDTO)          // Transforma cada Postulante → PostulanteDTO
    .toList();                             // Convierte stream a lista
    
    // DIRECCIÓN DETALLADA:
    // postulanteRepository.findByEstadoIn([POSTULADO, EN_EVALUACION])
    //   ↓ SQL
    //   SELECT * FROM postulantes 
    //   WHERE estado IN ('POSTULADO', 'EN_EVALUACION')
    //   ↓
    //   Retorna: List<Postulante> [
    //       Postulante {id:1, estado:POSTULADO, nombre:'Juan', ...},
    //       Postulante {id:2, estado:EN_EVALUACION, nombre:'Maria', ...},
    //       Postulante {id:3, estado:POSTULADO, nombre:'Carlos', ...}
    //   ]
    //   ↓
    //   .stream()
    //   Convierte a flujo de datos (patrón funcional)
    //   ↓
    //   .map(PostulanteMapper::toDTO)
    //   Para cada Postulante, aplica PostulanteMapper.toDTO()
    //   Postulante entity → PostulanteDTO
    //   ↓
    //   .toList()
    //   Recolecta todos los DTOs en una lista
    //   ↓
    //   Retorna: List<PostulanteDTO> [
    //       PostulanteDTO {id:1, estado:'POSTULADO', nombre:'Juan', ...},
    //       PostulanteDTO {id:2, estado:'EN_EVALUACION', nombre:'Maria', ...},
    //       PostulanteDTO {id:3, estado:'POSTULADO', nombre:'Carlos', ...}
    //   ]
}
```

---

### **ARCHIVO: OfertaServiceImpl.java**

#### **Método: guardar()**
```java
// LÍNEA 37-50
public OfertaDTO guardar(OfertaDTO dto) {
    // LÍNEA 38: Validar datos
    validar(dto);
    // DIRECCIÓN (línea 66-76):
    //   - Si dto es null → throw IllegalArgumentException("La oferta no puede ser nula.")
    //   - Si titulo es blank → throw IllegalArgumentException("El titulo es obligatorio.")
    //   - Si vacantes <= 0 → throw IllegalArgumentException("Las vacantes deben ser mayores a 0.")
    
    // LÍNEA 39: Crear nuevo entity
    OfertaLaboral oferta = new OfertaLaboral();
    
    // LÍNEA 40-44: Copiar datos del DTO al Entity
    oferta.setId(dto.getId());                           // ID (null si es nuevo)
    oferta.setTitulo(dto.getTitulo());                   // "Dev Java Senior"
    oferta.setDescripcion(dto.getDescripcion());         // "Descripción..."
    oferta.setVacantes(dto.getVacantes());               // 3
    oferta.setActiva(dto.getActiva() == null || dto.getActiva());  // true por defecto
    
    // LÍNEA 45-48: Asignar área si existe
    if (dto.getAreaId() != null) {
        // Obtener área de la BD
        Area area = areaRepository.findById(dto.getAreaId()).orElse(null);
        // [SQL] SELECT * FROM areas WHERE id = ? (o null si no existe)
        oferta.setArea(area);
    }
    
    // LÍNEA 49: GUARDAR EN BD
    return OfertaMapper.toDTO(ofertaRepository.save(oferta));
    // ofertaRepository.save(oferta)
    //   ↓ Si oferta.id == null:
    //   INSERT INTO ofertas_laborales 
    //   (titulo, descripcion, vacantes, activa, area_id) 
    //   VALUES (?, ?, ?, ?, ?)
    //   ↓ Si oferta.id != null:
    //   UPDATE ofertas_laborales SET ... WHERE id = ?
    //   ↓
    //   Retorna OfertaLaboral con id asignado (si fue insert)
    //   ↓
    //   OfertaMapper.toDTO() convierte a DTO
    //   ↓
    //   Retorna OfertaDTO
}
```

---

### **ARCHIVO: EvaluacionServiceImpl.java**

#### **Método: calificar()** - ¡¡¡ FLUJO MÁS COMPLEJO !!!
```java
// LÍNEA 41-64
public int calificar(Long postulacionId, Long usuarioId, Map<String, String> respuestas) {
    
    // LÍNEA 42-44: Validar permiso
    if (!puedeRendir(postulacionId, usuarioId)) {
        throw new IllegalStateException("No puedes rendir esta evaluacion.");
    }
    // puedeRendir() verifica:
    //   1. El postulante existe
    //   2. El usuario asociado es el que intenta rendir
    //   3. El estado es POSTULADO o EN_EVALUACION
    // Si alguno falla → lanza excepción
    
    // LÍNEA 45: Obtener postulante
    Postulante postulante = postulanteRepository.findById(postulacionId).orElseThrow();
    // [SQL] SELECT * FROM postulantes WHERE id = ?
    // Si no existe → NoSuchElementException
    
    // LÍNEA 46: Obtener preguntas de la oferta
    List<Pregunta> preguntas = preguntaRepository.findByOfertasId(postulante.getOferta().getId());
    // [SQL] SELECT p.* FROM preguntas p
    //       JOIN oferta_preguntas op ON p.id = op.pregunta_id
    //       WHERE op.oferta_id = ?
    // Retorna todas las preguntas de esta oferta
    
    // LÍNEA 48-53: ITERAR Y CALIFICAR RESPUESTAS
    int correctas = 0;
    for (Pregunta pregunta : preguntas) {
        // Por cada pregunta en la oferta:
        
        // LÍNEA 50: Obtener respuesta del usuario
        String respuesta = respuestas.get("respuesta_" + pregunta.getId());
        // En el Map: {"respuesta_1": "Respuesta A", "respuesta_2": "Respuesta B", ...}
        // Extrae la respuesta para esta pregunta específica
        
        // LÍNEA 51-53: Comparar con opción correcta
        if (respuesta != null && respuesta.trim().equalsIgnoreCase(pregunta.getOpcionCorrecta())) {
            correctas++;
            // Si la respuesta coincide (case-insensitive) → incrementar contador
        }
    }
    
    // LÍNEA 56: CALCULAR PUNTAJE
    int puntaje = preguntas.isEmpty() ? 0 : (correctas * 100) / preguntas.size();
    // Si hay 5 preguntas y respondió 3 correctamente:
    // puntaje = (3 * 100) / 5 = 60
    
    // LÍNEA 57: Determinar si está aprobado
    boolean aprobado = puntaje >= 60;
    // Nota de corte: 60 puntos
    
    // LÍNEA 58-61: Actualizar postulante con resultados
    postulante.setPuntaje(puntaje);           // 60
    postulante.setAprobado(aprobado);         // true
    postulante.setEstado(aprobado ? EstadoPostulante.APROBADO : EstadoPostulante.RECHAZADO);
    postulante.setObservacion(aprobado ? "Evaluacion aprobada." : "Evaluacion desaprobada.");
    
    // LÍNEA 62: GUARDAR CAMBIOS EN BD
    postulanteRepository.save(postulante);
    // [SQL] UPDATE postulantes 
    //       SET puntaje=60, aprobado=true, estado='APROBADO', observacion='...'
    //       WHERE id = ?
    
    // LÍNEA 63: Retornar puntaje
    return puntaje;  // 60
}
```

**Flujo visual de evaluación:**

```
Usuario envía respuestas:
{
  "respuesta_1": "Opción A",
  "respuesta_2": "Opción C", 
  "respuesta_3": "Opción B",
  "respuesta_4": "Opción A",
  "respuesta_5": "Opción D"
}
    ↓
calificar(postulacionId=15, usuarioId=5, respuestas)
    ↓
for (Pregunta p : preguntas) {
    Pregunta 1: usuario="A" vs correcta="A" ✓ correctas++  (correctas=1)
    Pregunta 2: usuario="C" vs correcta="B" ✗             (correctas=1)
    Pregunta 3: usuario="B" vs correcta="B" ✓ correctas++  (correctas=2)
    Pregunta 4: usuario="A" vs correcta="A" ✓ correctas++  (correctas=3)
    Pregunta 5: usuario="D" vs correcta="D" ✓ correctas++  (correctas=4)
}
    ↓
puntaje = (4 * 100) / 5 = 80
aprobado = 80 >= 60 → true
    ↓
postulante.estado = APROBADO
postulante.puntaje = 80
postulante.aprobado = true
    ↓
postulanteRepository.save()
    ↓
UPDATE BD
    ↓
return 80
```

---

## 🔀 Direccionamiento de Datos

### **Diagrama de flujo de un objeto Postulante**

```
1️⃣ FRONTEND (HTML Form)
   └─→ PostulanteDTO {
       nombre: "Juan Pérez",
       email: "juan@example.com",
       telefono: "987654321",
       ofertaId: 1
   }

2️⃣ CONTROLLER (PostulanteController)
   └─→ postular(PostulanteDTO, usuarioId=5)

3️⃣ SERVICE (PostulanteServiceImpl)
   ├─→ PostulanteMapper.toEntity(DTO)
   │  └─→ Postulante {
   │      nombre: "Juan Pérez",
   │      email: "juan@example.com",
   │      ...
   │  }
   ├─→ Enriquecer datos:
   │  ├─→ postulante.setEstado(POSTULADO)
   │  ├─→ usuarioRepository.findById(5)
   │  │  └─→ SQL: SELECT * FROM usuarios WHERE id=5
   │  │     └─→ Usuario {id:5, nombre:"Juan Pérez", email:"juan@example.com", ...}
   │  ├─→ postulante.setUsuario(usuario)
   │  └─→ ofertaRepository.findById(1)
   │     └─→ SQL: SELECT * FROM ofertas_laborales WHERE id=1
   │        └─→ OfertaLaboral {id:1, titulo:"Dev Java", vacantes:3, ...}
   │        └─→ postulante.setOferta(oferta)
   
4️⃣ REPOSITORY (PostulanteRepository)
   └─→ postulanteRepository.save(postulante)
      └─→ SQL INSERT:
          INSERT INTO postulantes 
          (nombre, email, telefono, estado, usuario_id, oferta_id, ...)
          VALUES ('Juan Pérez', 'juan@example.com', '987654321', 'POSTULADO', 5, 1, ...)
          └─→ Base de datos retorna: id=15 (auto-generado)

5️⃣ ENTITY (Postulante con ID)
   └─→ Postulante {
       id: 15,
       nombre: "Juan Pérez",
       email: "juan@example.com",
       telefono: "987654321",
       estado: POSTULADO,
       usuario: Usuario {id:5, ...},
       oferta: OfertaLaboral {id:1, ...},
       puntaje: 0,
       aprobado: false
   }

6️⃣ SERVICE (Mapper)
   └─→ PostulanteMapper.toDTO(entity)
      └─→ PostulanteDTO {
          id: 15,
          nombre: "Juan Pérez",
          email: "juan@example.com",
          estado: "POSTULADO",
          usuarioId: 5,
          ofertaId: 1,
          ...
      }

7️⃣ CONTROLLER (Retorna resultado)
   └─→ return "redirect:/postulante/mis-postulaciones"

8️⃣ FRONTEND
   └─→ Muestra lista de postulaciones del usuario
       ├─ Postulación 1: Dev Java (POSTULADO)
       ├─ Postulación 2: Dev Python (EN_EVALUACION)
       └─ Postulación 3: Data Engineer (APROBADO)
```

---

## 📝 Flujo de Evaluación

### **Caso: Admin evalúa a postulante**

```
INICIO: Estado BD
┌────────────────────────────────────┐
│ Postulante (id=15)                 │
│ - estado: POSTULADO                │
│ - puntaje: 0                       │
│ - aprobado: false                  │
│ - observacion: null                │
└────────────────────────────────────┘

    ↓ Admin marca para evaluación
    
LLAMADA: POST /admin/postulantes/estado/15/EN_EVALUACION
    ↓
PostulanteController.cambiarEstado(id=15, estado="EN_EVALUACION")
    ↓
PostulanteServiceImpl.cambiarEstado(15, "EN_EVALUACION")
    ↓
Postulante p = postulanteRepository.findById(15)
    ↓ SQL: SELECT * FROM postulantes WHERE id=15
p.setEstado(EstadoPostulante.EN_EVALUACION)
p.setAprobado(false)
postulanteRepository.save(p)
    ↓ SQL: UPDATE postulantes SET estado='EN_EVALUACION', aprobado=false WHERE id=15
    
ESTADO EN BD:
┌────────────────────────────────────┐
│ Postulante (id=15)                 │
│ - estado: EN_EVALUACION            │
│ - puntaje: 0                       │
│ - aprobado: false                  │
└────────────────────────────────────┘

    ↓ Postulante va a rendirla evaluación
    
LLAMADA: POST /evaluacion/calificar
    ↓
Respuestas: {respuesta_1: "A", respuesta_2: "C", ...}
    ↓
EvaluacionServiceImpl.calificar(15, 5, respuestas)
    
    DENTRO DE calificar():
    ├─→ Validar puedeRendir(15, 5) ✓
    ├─→ preguntas = [P1, P2, P3, P4, P5]
    ├─→ for (Pregunta p : preguntas) {
    │    comparar respuesta vs opcionCorrecta
    │  }
    ├─→ correctas = 4
    ├─→ puntaje = (4 * 100) / 5 = 80
    ├─→ aprobado = 80 >= 60 → true
    └─→ postulante.estado = APROBADO
        postulante.puntaje = 80
        postulanteRepository.save()
    
    ↓ SQL: UPDATE postulantes 
           SET estado='APROBADO', puntaje=80, aprobado=true, 
               observacion='Evaluacion aprobada.'
           WHERE id=15

ESTADO FINAL EN BD:
┌────────────────────────────────────┐
│ Postulante (id=15)                 │
│ - estado: APROBADO                 │
│ - puntaje: 80                      │
│ - aprobado: true                   │
│ - observacion: "Evaluacion aprobada."
└────────────────────────────────────┘

    ↓ Usuario ve su resultado
    
VISTA: /postulante/postulaciones/15/estado
    ├─ Nombre: Juan Pérez
    ├─ Oferta: Dev Java
    ├─ Estado: APROBADO ✅
    ├─ Puntaje: 80/100
    └─ Observación: Evaluacion aprobada.
```

---

## 📊 Tabla Resumen: Métodos y sus Destinos

| Método | En (Controller/Service) | Llama a | SQL Generado | Destino |
|--------|-----------|----------|----------|-----------|
| `formularioPostulacion()` | Controller | `ofertaService.buscar()` | SELECT FROM ofertas_laborales | Renderiza formulario |
| `postular()` | Controller | `postulanteService.postular()` | INSERT INTO postulantes | INSERT + redirect |
| `postular()` | Service | `usuarioRepository.findById()` | SELECT FROM usuarios | Enriquece datos |
| `postular()` | Service | `ofertaRepository.findById()` | SELECT FROM ofertas_laborales | Enriquece datos |
| `postular()` | Service | `postulanteRepository.save()` | INSERT INTO postulantes | Crea registro |
| `listarPorUsuario()` | Service | `postulanteRepository.findByUsuarioId()` | SELECT FROM postulantes WHERE usuario_id | Obtiene lista |
| `guardar()` | Service (Oferta) | `areaRepository.findById()` | SELECT FROM areas | Enriquece datos |
| `guardar()` | Service (Oferta) | `ofertaRepository.save()` | INSERT/UPDATE ofertas_laborales | Crea o actualiza |
| `calificar()` | Service (Evaluación) | `postulanteRepository.findById()` | SELECT FROM postulantes | Obtiene postulante |
| `calificar()` | Service (Evaluación) | `preguntaRepository.findByOfertasId()` | SELECT FROM preguntas | Obtiene preguntas |
| `calificar()` | Service (Evaluación) | `postulanteRepository.save()` | UPDATE postulantes | Actualiza calificación |

---

## 🎓 Conclusión

El flujo de servicios funciona mediante:

1. **SEPARACIÓN DE CAPAS**: Cada capa tiene responsabilidades claras
2. **INYECCIÓN DE DEPENDENCIAS**: Los servicios reciben sus dependencias
3. **MAPPERS**: Convierten entre DTOs y Entities
4. **REPOSITORIES**: Abstraen el acceso a datos
5. **TRANSACCIONES**: Garantizan integridad de datos
6. **VALIDACIONES**: En controladores y servicios

Cada línea de código se orienta a **obtener datos → procesarlos → guardarlos → retornarlos**.

---

**Autores**: Sistema de Reclutamiento - Construcción de Software  
**Versión**: 1.0  
**Fecha**: 2026-05-23

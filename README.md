# 🌌 Rick and Morty API Backend (Spring WebFlux)

> Proyecto reactivo con Spring WebFlux que consume la API pública de Rick and Morty y expone endpoints REST para una
> aplicación frontend.

---

## 🚀 Tecnologías utilizadas

- ☕ **Java 17**
- 🧰 **Spring Boot 3.4.4**
- 🔁 **Spring WebFlux** (programación reactiva)
- 🌐 **WebClient** para consumo de APIs
- 🧪 **JUnit 5** y **Mockito** para pruebas unitarias
- 📦 **Maven** como gestor de dependencias

---

## ✅ Requisitos cumplidos

### 🛰️ Consumo de API

- WebClient para llamadas reactivas a la API de Rick and Morty
- Peticiones asíncronas y no bloqueantes

### 🧬 Modelado de Datos

- Modelos: `Character`, `Location`, `Episode`, `Origin`
- Respuesta paginada con `CharacterResponse`

### 🎮 Controladores

- `CharacterController` con endpoints reactivos (`Mono` / `Flux`)
- Rutas para: listar, buscar por ID y nombre
- Configuración CORS

### 🚨 Gestión de Errores

- `GlobalExceptionHandler` para manejo centralizado
- `ResourceNotFoundException` para errores 404
- Respuestas con HTTP apropiados (`404`, `500`, etc.)
- Mensajes JSON informativos con timestamp

### 🧪 Testing

- Unitarios con **Mockito**
- WebTestClient para controladores
- Casos de error y prueba de integración

---

## 🌟 Valor agregado

- Arquitectura **100% reactiva** con Spring WebFlux
- 🧠 Mejor rendimiento para peticiones concurrentes
- 🚀 Caché para evitar llamadas repetidas
- ⏱️ Timeouts configurables a la API externa

---

## 📁 Estructura del proyecto

```
rickandmorty-api/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/rickandmorty/
│   │   │       ├── ReactiveRickAndMortyApplication.java
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── exception/
│   │   │       ├── model/
│   │   │       │   └── response/
│   │   │       └── service/
│   │   └── resources/
│   └── test/
│       └── java/
│           └── com/rickandmorty/
│               ├── controller/
│               ├── service/
│               └── integration/
└── pom.xml
```

---

## 🌐 Endpoints disponibles

| Método | Ruta                                 | Descripción                     |
|--------|--------------------------------------|---------------------------------|
| GET    | `/api/characters`                    | Lista de personajes (paginados) |
| GET    | `/api/characters/page/{page}`        | Personajes por página           |
| GET    | `/api/characters/{id}`               | Buscar por ID                   |
| GET    | `/api/characters/search?name={name}` | Buscar por nombre               |

---

## ⚙️ Configuración e instalación

### 📌 Prerrequisitos

- Java 17
- Maven 3.6+

### 🛠️ Pasos

```bash
git clone https://github.com/Yobel-SCM-2/rick-and-morty-back.git
cd rick-and-morty-back

mvn clean install
mvn spring-boot:run
mvn test
```

---

## 🖥️ Uso con el frontend

Este backend está pensado para integrarse con el frontend Angular disponible en:

[**Rick and Morty Frontend**](https://github.com/Yobel-SCM-2/rick-and-morty-front)

> Para una experiencia completa, ejecuta ambos proyectos simultáneamente.

---

## 📬 Contacto

¿Tienes preguntas o sugerencias? ¡No dudes en escribirme!

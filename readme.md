# 🏪 Tienda UPM — Práctica POO 2025-2026

## 📘 Descripción
Proyecto desarrollado como **Entrega 1 (E1)** de la asignatura **Programación Orientada a Objetos** (Grado en Ingeniería del Software, ETSISI-UPM) por el **grupo IWSIM22_09**.

El objetivo de esta versión es implementar un **módulo de tickets** que permita:
- Crear y gestionar productos con identificador, nombre, categoría y precio.
- Aplicar **descuentos automáticos por categoría** cuando hay más de un producto del mismo tipo.
- Emitir una **factura (ticket)** ordenada alfabéticamente por nombre de producto.
- Permitir reiniciar el ticket, modificar o eliminar productos, y mostrar el estado actualizado por consola.

---

## 🧩 Arquitectura del proyecto
El sistema sigue el patrón **MVC (Modelo–Vista–Controlador)**:

| Capa | Clases principales | Descripción |
|------|--------------------|--------------|
| **Modelo (`es.upm.etsisi.poo.model`)** | `Product`, `Category`, `Catalog`, `Ticket`, `TicketItem` | Representan los datos y la lógica de negocio: productos, categorías y tickets. |
| **Vista (`es.upm.etsisi.poo.view`)** | `ConsoleView` | Muestra mensajes e información al usuario mediante la consola. |
| **Controladores (`es.upm.etsisi.poo.controller`)** | `CommandController`, `ProductController`, `TicketController` | Interpretan comandos del usuario, coordinan operaciones entre modelo y vista. |
| **Aplicación (`es.upm.etsisi.poo`)** | `App` | Clase principal que inicia la aplicación y gestiona el ciclo de ejecución. |

---

## 💻 Comandos implementados
La aplicación acepta los siguientes comandos por consola:

```
prod add <id> "<nombre>" <categoria> <precio>
prod list
prod update <id> NAME|CATEGORY|PRICE <valor>
prod remove <id>
ticket new
ticket add <prodId> <cantidad>
ticket remove <prodId>
ticket print
echo "<texto>"
help
exit
```

📚 **Categorías disponibles:**  
`MERCH`, `STATIONERY`, `CLOTHES`, `BOOK`, `ELECTRONICS`

💸 **Descuentos aplicados (≥2 productos por categoría):**
- MERCH → 0 %
- STATIONERY → 5 %
- CLOTHES → 7 %
- BOOK → 10 %
- ELECTRONICS → 3 %

---

## ⚙️ Ejecución
1. Asegúrate de tener instalado **Java 17 o superior**.
2. Descarga el archivo `.jar` desde la sección 👉 **[Releases](../../releases)** del repositorio.
3. Ejecuta el programa con el siguiente comando:

```bash
java -jar tienda-upm-v1.0.0.jar
```

La aplicación mostrará el mensaje de bienvenida y quedará lista para recibir comandos.

---

## 📦 Estructura del repositorio
```
tienda-upm/
├── src/                          # Código fuente Java
├── docs/
│   └── uml-tienda-upm.png        # Diagrama UML del modelo
├── README.md                     # Este archivo
└── pom.xml / build.gradle (según el entorno de compilación)
```

🧠 El **diagrama UML** justificativo del modelo de clases se encuentra en la carpeta [`/docs`](./docs).

---

## 👥 Autores
| Nombre | Matrícula |
|--------|-----------|
| Tomás  | bv0374    |
| Marta  | bv0078    |
| Sofía  | bv0143    |
| Alicia | bv0195    |
| Jiling | bv0393    |

---

## 🗓️ Versión
**v1.0.0 — Primera entrega (12 de octubre 2025)**  
Código funcional y ejecutable, validado conforme al enunciado de la práctica “Tienda UPM”.

---

## 🧱 Entregables
- 🗂️ Código fuente completo en este repositorio.
- 🧾 Ejecutable `.jar` publicado en **Releases**.
- 🧩 Diagrama UML en `/docs`.

---

© 2025 ETSISI-UPM — Proyecto académico de Programación Orientada a Objetos.

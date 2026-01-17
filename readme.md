# 🏪 Tienda UPM — Práctica POO 2025-2026 (Entrega 3)

## 📘 Descripción

Proyecto desarrollado como **Entrega 3 (E3)** de la asignatura **Programación Orientada a Objetos** (Grado en Ingeniería del Software, ETSISI-UPM) por el **grupo IWSIM22_09**.

Esta entrega amplía y consolida el sistema desarrollado en la **Entrega 2**, incorporando **persistencia con JPA/Hibernate**, una **arquitectura desacoplada basada en repositorios**, y una separación más clara entre **modelo, repositorio, servicios y presentación**, manteniendo la interfaz CLI.

Las principales novedades de esta entrega son:

* **Persistencia en base de datos relacional** mediante **JPA / Hibernate**.
* Sustitución de repositorios en memoria por **repositorios Hibernate**.
* Uso de **entidades persistentes**, herencia JPA y relaciones (`@OneToMany`, `@ManyToOne`, `@ElementCollection`).
* **Estrategia de impresión de tickets** (Strategy Pattern) según tipo de cliente.
* Gestión avanzada de **productos, servicios y tickets combinados**.
* Mantenimiento completo de las **reglas de negocio y validaciones** de E2.

---

## 🧱 Entregables

* 🗂️ Código fuente completo en este repositorio.
* 🧾 Ejecutable `.jar` publicado en **Releases**.
* 🧩 Diagrama UML actualizado en `/docs`.
* 🗄️ Configuración de persistencia JPA (`persistence.xml`).

---

## 🧩 Arquitectura del proyecto

El sistema sigue una arquitectura **en capas**, con **inyección de dependencias manual** y persistencia desacoplada mediante repositorios.

| Capa                      | Paquetes principales                                 | Descripción                                                                   |
| ------------------------- | ---------------------------------------------------- | ----------------------------------------------------------------------------- |
| **Modelo**                | `es.upm.etsisi.poo.app3.data.model`                  | Entidades persistentes JPA: usuarios, productos, tickets y reglas de negocio. |
| **Repositorio**           | `es.upm.etsisi.poo.app3.data.repositories`           | Interfaces de acceso a datos independientes de la tecnología.                 |
| **Repositorio Hibernate** | `es.upm.etsisi.poo.app3.data.repositories.hibernate` | Implementaciones JPA/Hibernate con `EntityManager`.                           |
| **Servicios**             | `es.upm.etsisi.poo.app3.services`                    | Lógica de aplicación, validaciones y coordinación entre repositorios.         |
| **Vista**                 | `es.upm.etsisi.poo.app3.presentation.view`           | Salida por consola y presentación de resultados.                              |
| **CLI**                   | `es.upm.etsisi.poo.app3.presentation.cli`            | Interpretación y ejecución de comandos (interactivos o por fichero).          |
| **Aplicación**            | `es.upm.etsisi.poo.app3`                             | Inicialización, inyección de dependencias y arranque de la aplicación.        |

---

## 🗄️ Persistencia (Entrega 3)

En esta entrega:

* Todas las entidades principales están anotadas con **JPA** (`@Entity`).
* Se emplea **herencia JOINED** para usuarios y productos.
* Se utilizan relaciones:

  * `Client → Tickets` mediante `@ElementCollection`.
  * `Cashier → Ticket` mediante `@OneToMany`.
  * `Ticket → TicketItem` mediante `@OneToMany`.
* El acceso a la base de datos se realiza con **EntityManager** gestionado por `JPAUtil`.
* La aplicación es **independiente del motor** (H2, MySQL, etc.).

---

## 🎟️ Tickets y estrategia de impresión

Se aplica el **patrón Strategy** para la impresión de tickets:

* `TicketPrintingStrategy`

  * `PersonTicketPrinter`
  * `CompanyTicketPrinter`

La estrategia se asigna automáticamente según el **tipo de cliente**:

* **PERSON** → impresión detallada con precios y descuentos.
* **COMPANY** → separación de servicios y productos, con descuentos adicionales por servicios combinados.

---

## 💻 Comandos implementados

### Clientes / Cajeros

```
client add "<nombre>" (<DNI>|<NIF>) <email> <cashId>
client remove <ID>
client list

cash add [<id>] "<nombre>" <email>
cash remove <id>
cash list
cash tickets <id>
```

---

### Tickets

```
ticket new [<id>] <cashId> <userId> -[c|p|s]
ticket add <ticketId> <cashId> <prodId> <amount> [--p<texto> ...]
ticket remove <ticketId> <cashId> <prodId>
ticket print <ticketId> <cashId>
ticket list
```

**Notas:**

* `-p` (PRODUCT) es el tipo por defecto.
* `-c` → ticket combinado (productos + servicios).
* `-s` → solo servicios.
* Imprimir un ticket **lo cierra automáticamente**.
* Los clientes PERSON no pueden usar servicios.

---

### Productos y servicios

```
prod add [<id>] "<name>" <category> <price> [<maxTexts>]
prod addFood [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
prod addMeeting [<id>] "<name>" <price> <expiration:yyyy-MM-dd> <max_people>
prod update <id> NAME|CATEGORY|PRICE <value>
prod list
prod remove <id>
```

---

### Generales

```
help
echo "<texto>"
exit
```

---

## 📚 Categorías y descuentos

* Categorías: `MERCH`, `STATIONERY`, `CLOTHES`, `BOOK`, `ELECTRONICS`
* Descuentos por categoría si hay ≥ 2 unidades:

| Categoría   | Descuento |
| ----------- | --------- |
| MERCH       | 0 %       |
| STATIONERY  | 5 %       |
| CLOTHES     | 7 %       |
| BOOK        | 10 %      |
| ELECTRONICS | 3 %       |

---

## ⚙️ Ejecución

1. Asegúrate de tener **Java 22 o superior**.
2. Descarga el `.jar` desde **Releases**.
3. Ejecución interactiva:

```bash
java -jar tienda-upm-v3.0.0.jar
```

4. Ejecución con fichero de comandos:

```bash
java -jar tienda-upm-v3.0.0.jar input.txt
```

---

## 📦 Estructura del repositorio

```
tiendas-upm/
├── src/
│   └── main/java/es/upm/etsisi/poo/app3/
├── docs/
│   └── UML.pdf
├── db/
│   └── app3_db.mv.db
├── README.md
└── pom.xml
```

---

## 👥 Autores

| Nombre | Matrícula |
| ------ | --------- |
| Tomás  | bv0374    |
| Marta  | bv0078    |
| Sofía  | bv0143    |
| Alicia | bv0195    |
| Jiling | bv0393    |

---

## 🗓️ Versión

**v3.0.0 — Tercera entrega (E3, 2025-2026)**
Persistencia JPA/Hibernate, arquitectura desacoplada y sistema completo validado.

---

© 2025 ETSISI-UPM — Proyecto académico de Programación Orientada a Objetos.

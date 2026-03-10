# 📚 Library Inventory Management System (CLI)

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/Status-Completed-success?style=for-the-badge" alt="Status">
</p>

## 🎯 Descripción del Proyecto
Este sistema es una solución robusta de línea de comandos (CLI) diseñada para la gestión integral de una biblioteca. El proyecto fue desarrollado con el objetivo de profundizar en la **Programación Orientada a Objetos (POO)** y el manejo avanzado de **Estructuras de Datos** en Java, priorizando la lógica pura sobre el uso de librerías externas.

---

## 🛠️ Stack Tecnológico y Conceptos Aplicados

### **Core de Desarrollo**
* **Lenguaje:** Java 17+
* **Arquitectura:** Modelo-Controlador (Separación de responsabilidades).

### **Estructuras de Datos (Java Collections)**
* <kbd>Map</kbd> (`HashMap`): Implementado para el inventario y usuarios, garantizando búsquedas rápidas con complejidad **O(1)**.
* <kbd>Deque</kbd> (`ArrayDeque`): Utilizado como **Stack** para el historial de acciones (LIFO), permitiendo funciones de "Deshacer".
* <kbd>List</kbd> (`ArrayList`): Gestión dinámica de libros prestados por usuario.

### **Principios de Ingeniería**
* **POO:** Abstracción (clases abstractas), Herencia, Encapsulamiento y Polimorfismo.
* **Persistencia Nativa:** Serialización y deserialización manual de **JSON** utilizando `StringBuilder` y **Regex** avanzado para el parseo de datos.
* **Robustez:** Sistema de excepciones personalizadas (`ItemNotFoundException`, `UserNotFoundException`).

---

## ✨ Características Principales

<table>
  <tr>
    <td><b>📖 Gestión de Libros</b></td>
    <td>Registro, búsqueda por ID, listado completo y control de disponibilidad.</td>
  </tr>
  <tr>
    <td><b>👥 Control de Usuarios</b></td>
    <td>Registro de miembros y visualización de libros asociados a su cuenta.</td>
  </tr>
  <tr>
    <td><b>🔄 Préstamos y Devoluciones</b></td>
    <td>Lógica de validación que impide prestar libros no disponibles.</td>
  </tr>
  <tr>
    <td><b>🕒 Historial Inteligente</b></td>
    <td>Registro de acciones recientes con capacidad de <b>Undo (Deshacer)</b>.</td>
  </tr>
</table>

---

## 🚀 Instalación y Ejecución

### **Requisitos**
- JDK 17 o superior instalado.
- Terminal o consola de comandos.

### **Pasos**
1. **Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/nombre-del-repo.git](https://github.com/tu-usuario/nombre-del-repo.git)
   ```
2. **Compilar el proyecto:**
   ```bash
   javac -d bin src/com/cursos/biblioinventory/**/*.java
   ```
3. **Ejecutar la aplicación:**
   ```bash
   java -cp bin com.cursos.biblioinventory.Main
   ```
## 🧠 Retos Técnicos y Aprendizajes
<blockquote>
<b>El desafío del Parsing JSON:</b> Uno de los mayores retos fue reconstruir objetos complejos (especialmente las listas anidadas de libros dentro de los usuarios) sin usar herramientas como Jackson o Gson. Se implementó un motor de búsqueda de patrones basado en <b>Expresiones Regulares (Regex)</b> para separar los campos del archivo de texto y mapearlos correctamente a los atributos de las clases.
</blockquote>
<hr>
<p align="center">
Desarrollado con ❤️ como parte de un proceso de aprendizaje continuo en Java.
</p>

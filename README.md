# 📊 Sistema de Gestión en Java + MySQL para SAME San Pedro 

![Java](https://img.shields.io/badge/Java-11%2B-orange?logo=java&logoColor=white)  
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql&logoColor=white)  
![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)  
![Build](https://img.shields.io/badge/Build-Passing-brightgreen?logo=github)  

Bienvenido a **SAME REAL**, un sistema de gestión robusto, modular y escalable, diseñado para simplificar procesos administrativos y centralizar la información del SAME San Pedro con una base de datos confiable. 🚀  

## ✨ Características principales  

- 🔗 **Conexión segura** con MySQL  
- 🗂️ **Gestión completa de datos**: altas, bajas, modificaciones y consultas  
- 👨‍💻 **Interfaz intuitiva** en consola o GUI (según configuración)  
- 🛠️ **Arquitectura en capas**: separación clara entre lógica de negocio, persistencia y presentación  
- 📈 **Escalable** para crecer con las necesidades de la organización  

## 🛑 Requisitos previos  

Antes de ejecutar el sistema, asegúrate de contar con:  

- ☕ **Java JDK 11 o superior**  
- 🐬 **MySQL 8.0 o superior**  
- 🛢️ **Conector JDBC** (`mysql-connector-java-x.x.x.jar`)  
- 💻 IDE recomendado: **IntelliJ IDEA / Eclipse / NetBeans**  

## ⚙️ Instalación y configuración  

1. **Clonar el repositorio**  
   ```bash
   git clone https://github.com/Mosca912/SAMEREAL
   ```

2. **Configurar la base de datos MySQL**  
   - Crear una base de datos:  
     ```sql
     CREATE DATABASE sistema_gestion;
     ```
   - Importar el esquema incluido en la carpeta `db/`:  
     ```bash
     mysql -u root -p sistema_gestion < db/esquema.sql
     ```

3. **Configurar credenciales en el archivo de propiedades**  
   ```properties
   db.url=jdbc:mysql://localhost:3306/sistema_gestion
   db.user=root
   db.password=tu_contraseña
   ```

4. **Compilar y ejecutar el sistema**  
   ```bash
   javac -cp "lib/*" src/*.java -d bin/
   java -cp "bin:lib/*" Main
   ```

## 🖥️ Uso  

- 📌 Ejecuta el programa  
- 📂 Selecciona el módulo deseado:  
  - Gestión de usuarios 👤  
  - Gestión de ........ 📦  
  - Gestión de ........ 💰  
- 📝 Guarda los cambios automáticamente en MySQL  

## 🧩 Estructura del proyecto  

```
📦 sg-javysql
 ┣ 📂 src          # Código fuente en Java
 ┣ 📂 db           # Scripts SQL para la base de datos
 ┣ 📂 lib          # Librerías externas (MySQL Connector, etc.)
 ┣ 📂 bin          # Archivos compilados
 ┣ 📄 README.md    # Este archivo
 ┗ 📄 LICENSE
```

## 🚀 Futuras mejoras  

- 🌐 Interfaz web con **Spring Boot**  
- 🔒 Autenticación con roles y permisos  
- 📊 Panel gráfico de estadísticas en tiempo real  
- ☁️ Integración con servicios en la nube  

## 🤝 Contribuciones  

¡Las contribuciones son bienvenidas!  
Si tienes ideas, abre un **issue** o envía un **pull request**. 🙌  

## 📜 Licencia  

Este proyecto está bajo la licencia **MIT**. Puedes usarlo, modificarlo y distribuirlo libremente.  

---

💡 *“Un buen sistema de gestión no solo organiza datos, organiza el futuro.”* ✨  

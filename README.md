# Classifieds Platform — Full-Stack Web Application

**Semester project for the “Web-Based Applications” course**  
University of Applied Science RheinMain | Summer Semester 2026

---

## 📌 About the Project

Classifieds Platform is a full-stack web application for browsing and managing classified ads. It combines a Vue.js frontend with a Spring Boot backend, an in-memory H2 database, and real-time updates through WebSockets.

The Vue.js frontend allows users to browse listings, place orders, and cancel orders. Separate Thymeleaf-based administration pages provide tools for creating and managing listings and user accounts.

The project covers database modeling, REST API development, frontend implementation, and integration between the application layers.

---

## ✨ Features

- Browse classified ads in the Vue.js frontend
- Place and cancel orders
- Create, edit, and delete listings through the administration pages
- Manage user accounts through the administration pages
- Validate user input and display form errors
- Handle authentication and access control with Spring Security
- Store users, listings, and orders using JPA and H2
- Receive real-time updates through WebSockets and STOMP

---

## 🛠️ Technologies

### Frontend

- **Vue.js** — user interface
- **TypeScript** — application logic
- **Pinia** — state management
- **Vue Router** — navigation
- **Vite** — development server and build tooling
- **STOMP.js** — real-time messaging client

### Backend

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Thymeleaf**
- **H2 Database**
- **REST API**
- **WebSocket / STOMP**
- **MapStruct**
- **Gradle**

---

## 🧩 Architecture

The application consists of three main layers:

- **Frontend:** displays listings and handles user interactions using Vue.js and TypeScript.
- **Backend:** provides REST endpoints, processes orders, implements application logic, and serves the administration pages.
- **Data layer:** manages users, listings, and orders using JPA and an H2 database.

The Vue.js frontend communicates with the backend through REST endpoints and receives real-time updates through WebSockets and STOMP.

Administration pages are rendered by the backend using Thymeleaf.

---

## 💾 Database

The application uses an **in-memory H2 database**.

Data is available while the backend is running but is not retained after it is stopped or restarted.

---

## 🎓 Learning Outcomes

- Developing a full-stack web application
- Designing and integrating REST APIs
- Modeling database entities and relationships with JPA
- Building interfaces with Vue.js and TypeScript
- Managing frontend state with Pinia
- Creating server-rendered administration pages with Thymeleaf
- Implementing authentication and access control
- Validating form input and handling errors
- Integrating real-time communication with WebSockets and STOMP

---

Developed as part of my Media Informatics studies at RheinMain University of Applied Sciences.

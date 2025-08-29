# TaskMate App 📝

TaskMate is a task management application built with **Spring Boot**.  
It provides user authentication, task creation, and task tracking features with role-based access for Admins.

---

## 🚀 Features
- User Registration with Email & OTP validation
- Login with JWT Authentication (Access & Refresh Tokens)
- Task CRUD operations (Create, Read, Update, Delete)
- Track Pending & Completed Tasks
- Admin can view all registered users

---

## 📌 API Endpoints

### Authentication
- `POST /auth/validateEmail` → Validate user email with OTP  
- `POST /auth/register` → Register new user  
- `POST /auth/login` → Login & get tokens  
- `POST /auth/refresh` → Refresh token  
- `POST /account` → Get logged-in user profile  
- `GET /admin` → Get all users (Admin only)  

### Task Management
- `POST /task` → Create new task  
- `PUT /task` → Update task  
- `GET /task/{id}` → Get task by ID  
- `DELETE /task/{id}` → Delete task by ID  
- `GET /task/pending/{id}` → Get all pending tasks for user  
- `GET /task/completed/{id}` → Get all completed tasks for user  

---

## 🛠️ Tech Stack
- Java 17+
- Spring Boot  
- Spring Security + JWT  
- Swagger (API Documentation)  
- Database: (e.g. MySQL/PostgreSQL)  

---

## ▶️ Getting Started

1. Clone the repo:
   ```bash
   git clone https://github.com/abhaykumar-cell/taskmate.git
   cd taskmate

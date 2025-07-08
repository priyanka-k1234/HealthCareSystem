# Hybrid Healthcare Database System

## Project Overview

The **Hybrid Healthcare Database System** is a secure, scalable, and user-friendly healthcare management platform designed to handle diverse healthcare data efficiently. It integrates both SQL and NoSQL databases to manage structured data (such as user accounts, patient profiles, and appointments) and flexible, evolving data (such as medical histories and sensor readings).

This hybrid approach leverages PostgreSQL for reliable, transactional data storage and MongoDB for flexible document-based storage, providing a comprehensive solution for modern healthcare data management.

---

## Features

- **User Management:** Role-based access control with Admin, Doctor, and Patient roles.  
- **Patient Management:** Create, read, update, and delete (CRUD) operations for patient profiles with search functionality.  
- **Appointment Scheduling:** Patients and doctors can book and manage appointments with status tracking.  
- **Medical History:** Doctors can view detailed patient medical histories stored in MongoDB.  
- **Sensor Data Monitoring:** Real-time sensor data collection and display from medical devices.  
- **Dashboard & Reporting:** Interactive charts and statistics for appointments, patient demographics, and sensor trends.  
- **Security:** Password hashing with BCrypt, multi-factor authentication (MFA) support, and secure role-based authorization.

---

## Technologies Used

- **Backend:** Java, Spring Boot, Spring Data JPA, Spring Data MongoDB, Spring Security  
- **Databases:** PostgreSQL (SQL), MongoDB (NoSQL)  
- **Frontend:** Thymeleaf, Bootstrap 5  
- **Build Tool:** Maven  

---

## Getting Started

### Prerequisites

- Java 17 or higher  
- Maven 3.6+  
- PostgreSQL database (configured and running)  
- MongoDB database (configured and running)  

### Installation and Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/hybrid-healthcare-system.git
   cd hybrid-healthcare-system

Configure database connection properties:

Edit src/main/resources/application.properties to set your PostgreSQL and MongoDB connection details:

spring.datasource.url=jdbc:postgresql://localhost:5432/healthcare_system
spring.datasource.username=your_postgres_username
spring.datasource.password=your_postgres_password

spring.data.mongodb.uri=mongodb://localhost:27017/healthcare_system

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081

Ensure databases are running:

Start PostgreSQL and create the database healthcare_system if it does not exist.
Start MongoDB and ensure it is accessible.
Build the project:

mvn clean install

Run the application:

mvn spring-boot:run

Access the application:

Open your browser and navigate to http://localhost:8081.

Default User Credentials
Use the following default accounts to log in and explore the system:

Role	Username	Password	Description
Admin	admin	admin	Full system access and user management
Doctor 1	drsmith	docpass	Doctor with patient management privileges
Doctor 2	drjones	docpass2	Another doctor user
Patient 1	johndoe	patientpass	Patient user with appointment booking
Patient 2	janeroe	patientpass2	Another patient user
Patient 3	bobsmith	patientpass3	Additional patient user

Usage
Login:
Use the credentials above to log in.

User Roles:
Admin: Manage users and system settings.
Doctor: Manage patients, appointments, and view medical histories.
Patient: View personal information and book appointments.
Patient Management: Add, edit, and search patient records.
Appointment Scheduling: Book and manage appointments with status updates.
Medical History & Sensor Data: View detailed medical records and live sensor data.
Dashboard: Monitor system statistics and trends via interactive charts.


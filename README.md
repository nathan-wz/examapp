# Exam App

The Exam App is a desktop application designed to manage online/offline examinations efficiently. Built with Java, Spring Framework, and JavaFX (Scene Builder), it provides a smooth interface for students and administrators to create, manage, and take exams.  

## 🚀 Features
- **User Authentication** – Secure login for students and administrators.  
- **Exam Creation** – Admins can create, edit, and delete exams.  
- **Question Bank** – Support for multiple question types (MCQs, true/false, short answer).  
- **Exam Management** – Assign exams to students, set durations, and manage results.  
- **Student Interface** – Clean, responsive UI for attempting exams.  
- **Result Processing** – Automatic scoring and result generation.  
- **JavaFX GUI** – Designed with Scene Builder for an intuitive interface.  

## 🛠️ Tech Stack
- **Language**: Java  
- **Framework**: Spring Boot (backend services)  
- **Frontend (GUI)**: JavaFX (Scene Builder for UI design)  
- **Database**: MySQL
- **Build Tool**: Gradle  

## 📂 Project Structure
```bash
exam-app/
│── src/main/java/com/examapp/
│   ├── controller/     # JavaFX controllers  
│   ├── model/          # Entities (User, Exam, Question, Result)  
│   ├── repository/     # Spring Data JPA repositories  
│   ├── service/        # Business logic for exams and users  
│   └── ExamApp.java    # Main entry point  
│
│── src/main/resources/
│   ├── fxml/           # Scene Builder FXML files  
│   ├── application.properties  # DB and Spring configs  
│   └── static/         # Styles, icons, etc.  
│
│── pom.xml / build.gradle  # Dependencies  
│── README.md  
```  

## ⚙️ Installation

### Prerequisites
- Java 17+  
- Maven or Gradle  
- MySQL 
- JavaFX SDK (if not bundled)  
- Scene Builder (for UI editing)  

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/exam-app.git
   cd exam-app
   ```
2. Configure your database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/examapp
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Build and run the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

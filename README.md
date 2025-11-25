# Kristechna Gaming Shop - Inventory Management System

## Project Overview
A full-stack Spring Boot application for managing inventory in a gaming shop. The system handles parts (both in-house and outsourced) and products assembled from those parts with comprehensive business logic validation.

### Prerequisites
- Java 21
- Apache Maven

### Running the Application
1. Clone the repository
2. Execute: `mvn spring-boot:run`
3. Access the application at: http://localhost:8080
4. Database console available at: http://localhost:8080/h2-console
    - JDBC URL: `jdbc:h2:file:./data/kristechna-gaming-shop`
    - Username: `sa`
    - Password: `password`

## Key Business Logic Features

### Multi-Pack Part System
- Automatic creation of multi-pack parts when duplicate parts are attempted
- Clickable links generate "2x [Part Name]" with appropriate pricing
- Manual multi-pack creation supporting quantities from 2 to 10

### Price Validation System
- Product price must be greater than or equal to the sum of associated parts
- Prevents adding parts that would exceed product price constraints
- Real-time validation during product assembly process

### Inventory Management
- Parts inventory decreases when product inventory increases during assembly
- Buy Now functionality decreases product inventory without affecting parts
- Minimum and maximum inventory constraints enforced across all operations

### Sample Data Initialization
- Automatically loads 5 gaming parts and 5 gaming products on first application run
- Prevents overwriting existing data in the database
- Utilizes Set implementation to avoid duplicate entries

## Requirements Implementation Documentation

### A. Git Repository
Status: Completed as per external GitLab instructions

### B. README File Documentation
- File: README.md
- Content: Comprehensive documentation of changes for requirements C through J

### C. Customize HTML User Interface
- File: src/main/resources/templates/main.html
- Lines: 5-15 (Header section with shop branding)
- Lines: 40-55 (Parts table with customized part names)
- Lines: 75-90 (Products table with customized product names)
- Change: Implemented "Kristechna: Connect. Evolve. Upgrade." branding with gaming-themed nomenclature

### D. About Page Implementation
- File: src/main/resources/templates/about.html
- File: src/main/java/com/kristechna/controller/MainController.java
- Change: Added comprehensive about page with company description and bidirectional navigation

### E. Sample Inventory System
- File: src/main/java/com/kristechna/service/InventoryServiceImpl.java
- Lines: 154-210 (addSampleInventory method)
- Change: Implemented sample inventory with 5 parts and 5 products using Set-based duplicate prevention and multi-pack creation logic

### F. Buy Now Button Implementation
- File: src/main/java/com/kristechna/controller/ProductController.java
- Lines: 189-202 (buyProduct method)
- File: src/main/resources/templates/main.html
- Lines: 85-86 (Button placement adjacent to update/delete actions)
- Change: Implemented Buy Now functionality with inventory decrement and success/failure messaging

### G. Maximum and Minimum Inventory Tracking
- File: src/main/java/com/kristechna/model/Part.java
- Change: Added minInv and maxInv fields to Part entity
- File: src/main/java/com/kristechna/service/InventoryServiceImpl.java
- Lines: 154-210 (Sample inventory includes min/max values)
- File: src/main/resources/templates/inhouse-part-form.html
- Lines: 60 (Min/max inventory input fields)
- File: src/main/resources/templates/outsourced-part-form.html
- Lines: 59 (Min/max inventory input fields)
- File: src/main/resources/application.properties
- Line: 6 (Storage file renamed to kristechna-gaming-shop)
- Change: Comprehensive min/max inventory implementation with file system update

### H. Validation for Maximum and Minimum Fields
- File: src/main/java/com/kristechna/controller/PartController.java
- Lines: 35-45 (Error handling for parts inventory validation)
- File: src/main/java/com/kristechna/controller/ProductController.java
- Lines: 148-168 (Error handling when product updates affect parts inventory)
- Change: Implemented comprehensive validation with appropriate error messaging

### I. Unit Testing Implementation
- File: src/test/java/com/kristechna/PartTest.java
- Lines: 12-34 (testInhousePartInventoryValidationWithinRange)
- Lines: 37-64 (testOutsourcedPartMinMaxInventoryConstraints)
- Change: Implemented two unit tests validating maximum and minimum field functionality

### J. Code Quality Maintenance
- Status: No unused validator classes identified - utilizing Spring Boot validation annotations
- Change: Maintained clean codebase through removal of redundant validation classes

### K. Professional Communication Standards
- Files: All Java classes, HTML templates, CSS, and documentation
- Content: Professional code comments, user-friendly error messages, and comprehensive documentation

## Technical Architecture

### File Structure
src/
├── main/java/com/kristechna/
│ ├── controller/
│ │ ├── MainController.java
│ │ ├── PartController.java
│ │ └── ProductController.java
│ ├── model/
│ │ ├── Part.java
│ │ ├── InhousePart.java
│ │ ├── OutsourcedPart.java
│ │ └── Product.java
│ ├── service/
│ │ ├── InventoryService.java
│ │ └── InventoryServiceImpl.java
│ └── repository/
│ ├── PartRepository.java
│ └── ProductRepository.java
├── main/resources/
│ ├── templates/
│ │ ├── main.html
│ │ ├── about.html
│ │ ├── inhouse-part-form.html
│ │ ├── outsourced-part-form.html
│ │ └── product-form.html
│ ├── static/
│ │ └── style.css
│ └── application.properties
└── test/java/com/kristechna/
└── PartTest.java

### Technology Stack
- Java 21
- Spring Boot 3.5.7
- Spring Data JPA
- H2 Database
- Thymeleaf Template Engine
- Spring Validation
- Maven Build Tool
- JUnit 5 Testing Framework

### Dependencies
As specified in pom.xml:
- Spring Boot Starter Data JPA
- Spring Boot Starter Thymeleaf
- Spring Boot Starter Validation
- Spring Boot Starter Web
- Spring Boot DevTools (runtime)
- H2 Database (runtime)

- Spring Boot Starter Test (test scope)

## Screenshots

<img width="1758" height="1140" alt="Screenshot 2025-11-11 132201" src="https://github.com/user-attachments/assets/dd61d1e1-f362-493a-90e8-076fa93dad55" />

<img width="1475" height="951" alt="Screenshot 2025-11-11 132233" src="https://github.com/user-attachments/assets/8243b950-0df9-49b9-bef1-f75e4f288e34" />

<img width="1532" height="1062" alt="Screenshot 2025-11-11 132340" src="https://github.com/user-attachments/assets/5f50e8f5-c489-4c85-8f63-d89c456f2ee9" />



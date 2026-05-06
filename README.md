# Traineeship Management System
 
A Spring Boot web application for managing university traineeships, developed as part of the Software Engineering course project. The application supports students, companies, professors, and traineeship committee members in the management, assignment, supervision, and evaluation of traineeship positions.
 
The project follows an MVC architecture and applies Enterprise Application Architecture and GoF Design Patterns such as Strategy and Factory patterns.
 
---
 
## Features
 
### Student Functionalities
- Create and edit student profile
- Add interests, skills, and preferred location
- Apply for traineeship positions
- View assigned traineeship
- Fill traineeship logbook
 
### Company Functionalities
- Create company profile
- Announce available traineeship positions
- View available and assigned positions
- Evaluate traineeship students
 
### Professor Functionalities
- Create professor profile
- View supervised traineeships
- Evaluate traineeship students and companies
 
### Committee Member Functionalities
- View students who applied for positions
- Search suggested positions using:
  - Interests
  - Preferred location
  - Combined strategy
- Assign traineeship positions to students
- View in-progress traineeships
- Assign supervising professors using:
  - Interests-based strategy
  - Load-based strategy
- Complete traineeship process with pass/fail evaluation
 
---
 
## Architecture
 
The application follows the Spring Boot MVC Architecture:
 
- Controllers handle HTTP requests
- Services implement the business logic
- Repositories (Mappers) handle database communication
- Domain Model contains the entity classes
- Views are implemented using Thymeleaf templates
 
---
 
## Design Patterns Used
 
### Strategy Pattern
 
Implemented for:
 
#### Position Search Strategies
- SearchBasedOnInterests
- SearchBasedOnLocation
- CompositeSearch
 
#### Supervisor Assignment Strategies
- AssignmentBasedOnInterests
- AssignmentBasedOnLoad
 
### Factory Pattern
 
Implemented through:
- PositionsSearchFactory
- SupervisorAssignmentFactory
 
---
 
## Technologies Used
 
- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate
- Thymeleaf
- MySQL
- Maven
- HTML/CSS
 
---
 
## Project Structure
 
```text
src/main/java/com/traineeshipApp
│
├── controllers
├── services
├── mappers
├── domainmodel
├── search
├── assignments
├── config
│
src/main/resources
│
├── templates
├── static
└── application.properties
```
 
---
 
## Search and Matching Logic
 
The application supports matching between students and traineeship positions.
 
### Interests-Based Search
- Student interests are tokenized using comma-separated keywords
- Position topics are tokenized similarly
- Matching is based on overlap similarity
 
### Location-Based Search
- Student preferred location is matched against company location
 
### Combined Strategy
- Combines both interests and location criteria
 
---
 
## Supervisor Assignment Logic
 
Supervisors are assigned using two alternative strategies.
 
### Interests-Based Assignment
Professor interests are matched with traineeship topics.
 
### Load-Based Assignment
The professor with the minimum supervision load is selected.
 
---
 
## Database
 
The application uses MySQL and JPA/Hibernate entity relationships:
- One-to-Many
- Many-to-Many
- One-to-One
 
The domain model includes:
- Student
- Professor
- Company
- TraineeshipPosition
- Evaluation
- CommitteeMember
- User
 
---
 
## Running the Project
 
### Clone the Repository
 
```bash
git clone https://github.com/YOUR_USERNAME/traineeship-management-system.git
```
 
### Configure MySQL
 
Create a MySQL database:
 
```sql
CREATE DATABASE traineeshipdb;
```
 
Update `application.properties`:
 
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/traineeshipdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```
 
### Run the Application
 
Using Maven:
 
```bash
mvn spring-boot:run
```
 
Or run:
 
```text
TraineeshipAppApplication.java
```
 
---
 
## Functionalities Implemented
 
- Authentication and Authorization
- Student applications
- Position announcements
- Search strategies
- Position assignment
- Supervisor assignment
- In-progress traineeship monitoring
- Evaluation handling
- MVC-based UI with Thymeleaf
 
---
 
## References
 
- Martin Fowler – Enterprise Application Architecture
- GoF Design Patterns
- Spring Boot Documentation
- Thymeleaf Documentation
 
---
 
## Author
 
Developed for the Software Engineering course project at the University of Ioannina.

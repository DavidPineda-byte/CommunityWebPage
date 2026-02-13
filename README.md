
CommunityWebPage

Overview

CommunityWebPage is a full-stack Spring Boot web application built and deployed in a production-style cloud environment.
This project demonstrates backend architecture, REST API development, authentication and authorization using Spring Security, 
database integration with JPA/Hibernate, frontend rendering with Thymeleaf and JavaScript, and real-world cloud deployment on AWS EC2 
with Nginx reverse proxy configuration.

The application allows user registration and login, role-based access control (ADMIN and USER), poem management, and dynamic content rendering.

This project was intentionally built and deployed in a way that reflects real production workflows, including server setup, firewall configuration, 
reverse proxy debugging, database configuration, and redeployment cycles.

⸻

Tech Stack

Backend
	•	Java 21
	•	Spring Boot
	•	Spring Security
	•	Spring Data JPA
	•	Hibernate
	•	MySQL / MariaDB

Frontend
	•	Thymeleaf
	•	HTML5
	•	CSS3
	•	JavaScript (Fetch API)

Infrastructure
	•	AWS EC2 (Linux)
	•	Nginx (Reverse Proxy)
	•	MariaDB (Production Database)
	•	SSH-based deployment
	•	Manual build and redeploy workflow

⸻

Core Features

Authentication & Authorization
	•	Custom UserDetailsService implementation
	•	Role-based route protection (ADMIN vs USER)
	•	Secure endpoint configuration via SecurityFilterChain
	•	Custom login page
	•	JSON-based user registration endpoint
	•	Session-based authentication handling

REST API Design
	•	/api/** endpoints for frontend interaction
	•	JSON request/response handling
	•	Fetch API integration from frontend
	•	Clear separation between REST controllers and view controllers

Poem Management
	•	ADMIN-only poem creation endpoint
	•	Public poem viewing
	•	Proper newline preservation for stanza formatting
	•	Database storage of long-form text content

Database Integration
	•	JPA entity modeling
	•	Automatic schema management
	•	Secure production database configuration
	•	Remote database access setup via MariaDB
	•	Handling of text-length constraints and schema adjustments

⸻

Production Deployment (AWS)

This application was deployed manually to an AWS EC2 Linux instance to simulate real production infrastructure.

Server Configuration
	•	EC2 instance configured with:
	•	Port 22 (SSH)
	•	Port 80 (HTTP)
	•	Port 3306 (Restricted MySQL access)
	•	Installed:
	•	Java 21
	•	MariaDB
	•	Nginx

Reverse Proxy Setup

Nginx was configured to forward:

Internet → Port 80 → Nginx → Spring Boot (Port 8080)

This removed the need to expose port 8080 publicly and mirrors how real production systems operate behind reverse proxies.

DNS & Domain Configuration
	•	Route 53 hosted zone configured
	•	Domain pointed to EC2 public IP
	•	Verified DNS resolution
	•	Production testing using curl and browser verification

⸻

Deployment Workflow

This project uses a manual deployment workflow to demonstrate understanding of Linux-based production environments.
	1.	Modify application locally
	2.	Build executable JAR:

mvn clean package


	3.	Upload JAR to EC2:

scp -i key.pem target/app.jar ec2-user@EC2_IP:/home/ec2-user/


	4.	SSH into EC2
	5.	Stop existing Spring process:

ps aux | grep java
kill PID


	6.	Start new version:

nohup java -jar app.jar &


	7.	Verify application:

curl localhost



This demonstrates:
	•	Linux process management
	•	Server-level debugging
	•	Redeployment cycles
	•	Reverse proxy verification
	•	Production diagnostics

⸻

Challenges Solved

During development and deployment, the following production-level issues were diagnosed and resolved:
	•	Spring Security route misconfiguration
	•	Role-based endpoint conflicts
	•	SQL text length limitations
	•	Remote database binding configuration
	•	EC2 firewall misconfiguration (port 80 exposure)
	•	502 Bad Gateway errors due to application startup timing
	•	Nginx configuration restructuring
	•	Remote database access via MySQL Workbench
	•	Synchronizing database bind-address and AWS security groups

⸻

Skills Demonstrated

This project demonstrates:
	•	Backend system architecture
	•	Secure authentication systems
	•	RESTful API implementation
	•	Database schema modeling
	•	Cloud infrastructure deployment
	•	Linux server management
	•	Reverse proxy configuration
	•	Firewall and network debugging
	•	Real-world production troubleshooting

From entity modeling to domain routing, this project reflects full lifecycle ownership of a web application.

⸻

Future Improvements

Infrastructure & DevOps
	•	Automate deployment using CI/CD (GitHub Actions)
	•	Convert Spring process into a systemd service
	•	Add HTTPS using Let’s Encrypt
	•	Dockerize application for environment consistency
	•	Add monitoring and centralized logging

Security Enhancements
	•	Replace root DB access with restricted production database user
	•	Add email verification for new registrations
	•	Implement rate limiting
	•	Strengthen validation and password policies
	•	Standardize REST error responses

Application Features
	•	Pagination and sorting for poems
	•	Search functionality
	•	Image upload support (S3 integration)
	•	Rich text editor for poem creation
	•	User profile dashboard

Architecture Improvements
	•	Introduce DTO layer for stricter separation
	•	Implement centralized global exception handling
	•	Add caching for frequently accessed content
	•	Improve modularization for future scalability

⸻

Summary

CommunityWebPage is not simply a CRUD demonstration. It represents full-stack engineering across:
	•	Backend development
	•	Security configuration
	•	Database management
	•	Frontend integration
	•	Cloud deployment
	•	Infrastructure troubleshooting

It reflects hands-on experience building and deploying a real web application in a production-like environment.

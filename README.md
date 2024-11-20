# S5.02SpringAvanzat

Pet Management - Reactive API

This project is an application for managing pets, designed using a Spring WebFlux reactive BackEnd and a FrontEnd under development with React. The application allows users to interact with their pets and administrators to manage all pets.
Project Features

    Pet Management: Full CRUD functionality for pets.
    Reactive Programming: Built with Spring WebFlux for improved efficiency and scalability.
    Authentication and Roles:
        Users (user): Limited interaction with their own pets.
        Administrators (admin): Full management of pets and users.
    Database: Reactive integration with MongoDB.
    Documentation: Swagger UI to explore the endpoints.

Technologies Used
BackEnd

    Spring Boot: Main framework.
    WebFlux: For reactive handling.
    MongoDB: Reactive database.
    JWT: Secure authentication.

FrontEnd

    React: For user interface development.

Architecture

The project architecture follows the client-server model:

    FrontEnd:
        Graphical interface for user interaction.
        State management and communication with the BackEnd through REST APIs.
    BackEnd:
        Exposure of REST endpoints.
        Business logic and data persistence.

Key Endpoints
Pets (/pet)
Method	Route	Description
GET	/all	Retrieves all pets.
POST	/add	Adds a new pet.
PUT	/update	Updates an existing pet's details.
DELETE	/delete	Deletes a pet.
Installation and Setup
Prerequisites

    Java 17+
    Node.js (for FrontEnd).
    MongoDB installed and configured.

BackEnd Setup

    Clone the repository:

[git clone https://github.com/your-repository.git](https://github.com/davidcuadrado/S5.02SpringAvanzat).git
cd S05T02

Run the project with Maven:

    ./mvnw spring-boot:run

FrontEnd Setup

    Navigate to the FrontEnd directory:

cd pet-front

Install dependencies:

npm install

Run the development server:

    npm start

Security

    JWT implementation to protect the endpoints.
    Roles and permissions:
        User: Access to their own data.
        Administrator: Global access.

Next Steps

    Complete FrontEnd development.
    Continuous integration and unit testing.
    Improvements in user experience.

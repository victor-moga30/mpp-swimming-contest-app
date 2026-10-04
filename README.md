# Swimming Contest Management Application

A full-stack client-server application for managing registrations and events in a swimming competition.

The project was developed as part of the **Systems for Design and Implementation (MPP)** course and progressively extended from a Java desktop application into a distributed system with **gRPC communication, persistence, REST services, authentication, real-time notifications, and a React web client**.

## Features

- User authentication for competition operators
- Management of swimming contest events
- Registration of participants for eligible events
- Age-based event eligibility validation
- Search and filtering of participants and events
- Persistent storage using SQLite
- Hibernate ORM for database persistence
- Desktop client built with JavaFX
- Client-server communication using gRPC and Protocol Buffers
- Real-time server-to-client updates using gRPC streaming
- REST API implemented with Spring Boot
- JWT-based authentication and authorization
- WebSocket support for real-time web notifications
- React web interface for interacting with REST services
- Dedicated Java client for testing and consuming the REST API

## Architecture

The application follows a modular client-server architecture.

```text
mpp-swimming-contest-app
│
├── common
│   ├── Domain models
│   ├── DTOs
│   ├── Repository interfaces
│   └── Protocol Buffer / gRPC definitions
│
├── server
│   ├── Business logic
│   ├── Repository implementations
│   ├── Hibernate persistence
│   └── gRPC server
│
├── client
│   └── JavaFX desktop client
│
├── rest-server
│   ├── Spring Boot REST API
│   ├── Spring Security
│   ├── JWT authentication
│   └── WebSocket notifications
│
├── rest-client-java
│   └── Java REST client
│
└── web-client-react
    └── React + Vite web client
```

The shared `common` module contains the domain model and communication contracts used by the Java components.

The desktop application communicates with the server through **gRPC**, while the web application interacts with the system through the **Spring Boot REST API**.

## Technologies

**Backend**
- Java 17
- Spring Boot 3
- Spring Security
- Hibernate ORM
- SQLite
- gRPC
- Protocol Buffers
- WebSockets
- JWT
- Log4j2

**Desktop**
- JavaFX

**Frontend**
- React 19
- Vite
- JavaScript

**Build & Testing**
- Gradle
- JUnit 5

## Communication

### gRPC

The Java desktop client uses **gRPC** for communication with the server.

The communication contract is defined using Protocol Buffers, allowing the required Java stubs to be generated automatically during the Gradle build.

In addition to regular RPC calls, the application uses **server-side streaming** to propagate updates to connected clients when the state of the competition changes.

### REST API

A separate Spring Boot module exposes application functionality through a REST API.

The API provides endpoints for managing contest data and is protected using **Spring Security and JWT authentication**.

### Real-Time Updates

The system supports real-time communication through:

- gRPC streaming for the Java desktop client
- WebSocket notifications for the web client

This allows connected clients to receive updates without continuously polling the server.

## Persistence

Application data is stored in a **SQLite** database.

The persistence layer uses **Hibernate ORM** and follows the Repository pattern, separating database operations from the application's business logic.

This provides a clear separation between:

```text
UI → Communication Layer → Service Layer → Repository Layer → Database
```

## Web Client

The web interface is implemented using **React** and **Vite**.

It communicates with the Spring Boot backend through HTTP REST requests and supports authenticated access using JWT tokens.

During local development, the frontend runs on:

```text
http://localhost:5173
```

and the REST API runs on:

```text
http://localhost:8080
```

CORS is configured on the backend to allow communication between the React development server and the REST API.

## Running the Project

### Requirements

- Java 17+
- Node.js and npm
- Git

Clone the repository:

```bash
git clone https://github.com/victor-moga30/mpp-swimming-contest-app.git
cd mpp-swimming-contest-app
```

### Build the Java modules

On Windows:

```bash
gradlew.bat build
```

On Linux/macOS:

```bash
./gradlew build
```

### Start the Java server

```bash
gradlew.bat :server:run
```

### Start the JavaFX client

```bash
gradlew.bat :client:run
```

### Start the REST server

```bash
gradlew.bat :rest-server:bootRun
```

The REST API will be available at:

```text
http://localhost:8080
```

### Start the React client

```bash
cd web-client-react
npm install
npm run dev
```

The web application will be available at:

```text
http://localhost:5173
```

## What This Project Demonstrates

This project demonstrates practical experience with:

- designing multi-module Java applications
- implementing layered application architectures
- client-server communication
- gRPC and Protocol Buffers
- REST API design
- ORM and database persistence
- authentication with JWT
- real-time communication
- desktop development with JavaFX
- frontend development with React
- integrating multiple clients with a shared backend

## Author

**Victor Moga**

Computer Science student at Babeș-Bolyai University.

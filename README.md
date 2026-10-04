# Swimming Contest Management Platform

A multi-client distributed application for managing participants, events, and registrations in a swimming contest.

The project demonstrates several communication and application architectures within the same system, including a JavaFX desktop client, a Spring Boot REST API, a React web client, gRPC-based communication, JWT authentication, WebSocket support, and database persistence.

## Features

- Manage contest participants and events
- Register participants for eligible swimming events
- Validate participant eligibility based on age
- Search and retrieve participant and registration data
- Multi-client architecture
- JavaFX desktop client
- React web client
- REST API
- JWT-based authentication
- Real-time communication support
- gRPC communication with Protocol Buffers
- Server-side streaming for client updates
- SQLite persistence
- Hibernate-based data access

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.3
- Spring Security
- Hibernate
- SQLite
- Gradle

### Communication
- REST
- gRPC
- Protocol Buffers
- WebSocket
- Server-side streaming

### Frontend
- React 19
- Vite
- JavaFX 17
- FXML

### Security
- JWT authentication
- Spring Security

## Architecture

The repository is organized as a multi-module Gradle project.

```text
mpp-swimming-contest-app/
│
├── common/
│   ├── shared domain models
│   ├── communication contracts
│   └── Protocol Buffer definitions
│
├── server/
│   ├── business logic
│   ├── database persistence
│   └── gRPC server
│
├── client/
│   └── JavaFX desktop client
│
├── rest-server/
│   ├── Spring Boot REST API
│   ├── authentication and security
│   └── WebSocket support
│
├── rest-client-java/
│   └── Java REST client
│
└── web-client-react/
    └── React web application
```

The application separates shared domain logic, server-side functionality, communication layers, and client implementations into independent modules.

## Domain Model

The main entities of the application are:

### Participant

Represents a participant registered in the contest.

Typical information includes:

- identifier
- name
- personal identifier
- age

### Event

Represents a swimming event.

Each event contains information such as:

- event name
- distance
- minimum age
- maximum age

Eligibility can be checked based on the participant's age.

### Registration

Represents the association between a participant and a swimming event.

### User

Represents an application operator who can authenticate and manage contest data.

## Communication Layers

### REST API

The `rest-server` module exposes application functionality through a Spring Boot REST API.

The REST backend uses:

- Spring Boot
- Spring Security
- JWT
- Hibernate
- SQLite
- WebSocket support

A dedicated Java REST client is also included in the project.

### gRPC

The application also implements gRPC communication between Java clients and the server.

The communication contract is defined using Protocol Buffers.

```text
common/src/main/proto/contest.proto
```

The generated Java stubs are used by the server and clients to invoke remote procedures.

The gRPC service extends:

```text
ContestRpcGrpc.ContestRpcImplBase
```

and delegates application operations to the existing service layer.

### Real-Time Updates

The gRPC implementation supports server-side streaming through:

```text
SubscribeUpdates
```

Connected clients can subscribe to application updates and receive notifications when relevant changes occur, such as a new participant registration.

The REST module also includes WebSocket support for real-time communication.

## Clients

### JavaFX Desktop Client

The desktop application is implemented using:

- Java 17
- JavaFX 17
- FXML

It provides a graphical interface for interacting with the contest management system.

### React Web Client

The web client is implemented using:

- React 19
- Vite
- React DOM

It communicates with the application's REST services and provides a browser-based interface for contest management.

## Persistence

Application data is persisted using:

- SQLite
- Hibernate ORM

The server layer handles database access while keeping persistence logic separated from the presentation layer.

## Security

The REST backend uses Spring Security and JWT-based authentication.

Authentication is handled by the server, allowing protected API operations to be accessed only by authenticated users.

## Project Modules

| Module | Purpose |
|---|---|
| `common` | Shared models, interfaces, gRPC contracts, and generated communication classes |
| `server` | Core server-side logic, persistence, and gRPC services |
| `client` | JavaFX desktop application |
| `rest-server` | Spring Boot REST API, security, JWT, and WebSocket functionality |
| `rest-client-java` | Java client for the REST API |
| `web-client-react` | React/Vite web client |

## Running the Project

### Requirements

- Java 17
- Node.js
- npm
- Gradle

The project includes the Gradle Wrapper, so a separate Gradle installation is not required.

### Build the Java Modules

On Windows:

```bash
gradlew.bat build
```

On Linux/macOS:

```bash
./gradlew build
```

### Run the JavaFX Client

```bash
./gradlew :client:run
```

On Windows:

```bash
gradlew.bat :client:run
```

### Run the REST Server

```bash
./gradlew :rest-server:bootRun
```

On Windows:

```bash
gradlew.bat :rest-server:bootRun
```

### Run the React Client

```bash
cd web-client-react
npm install
npm run dev
```

### Build the React Client

```bash
npm run build
```

## Key Concepts Demonstrated

This project demonstrates:

- multi-module application design
- layered software architecture
- client-server communication
- REST API development
- gRPC and Protocol Buffers
- real-time server-to-client streaming
- WebSocket integration
- JWT authentication
- JavaFX desktop development
- React frontend development
- relational database persistence
- Hibernate ORM
- separation of domain, service, persistence, and presentation layers

## Repository Structure

```text
.
├── client/
├── common/
├── rest-client-java/
├── rest-server/
├── server/
├── web-client-react/
├── gradle/
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## Purpose

The project was developed as a distributed systems application demonstrating multiple approaches to client-server communication and frontend development within a shared domain.

Rather than relying on a single communication mechanism, the system explores REST, gRPC, streaming, WebSocket communication, desktop UI development, and a modern React frontend while sharing the same core contest-management domain.

# MPP – Problema 8 (Java)

Acest repository contine implementarea partiala a problemei 8 din cadrul laboratorului MPP.  
In aceasta etapa este implementat **modelul domeniului si interfetele repository** pentru accesul la date.

Aplicatia modeleaza un sistem pentru **gestionarea inscrierilor copiilor la un concurs de atletism**.

---

# Structura proiectului

Proiectul este organizat in doua pachete principale:

```
ro.mpp2026
 ├── model
 └── repository
```

---

# Model

Pachetul `model` contine entitatile domeniului.

### Child
Reprezinta un copil participant la competitie.

Campuri:
- `id`
- `name`
- `cnp`
- `age`

---

### Event
Reprezinta o proba sportiva.

Campuri:
- `id`
- `name`
- `distance`
- `minAge`
- `maxAge`

Contine metoda:
- `isAllowedForAge(int age)` – verifica daca un copil poate participa la proba in functie de varsta.

---

### Registration
Reprezinta inscrierea unui copil la o proba.

Campuri:
- `id`
- `childId`
- `eventId`

---

### User
Reprezinta un utilizator al aplicatiei (operator de la un oficiu).

Campuri:
- `id`
- `username`
- `passwordHash`
- `office`

---

# Repository

Pachetul `repository` contine interfete pentru accesul la date.

### ChildRepository

Operatii:
- `findByCnp(String cnp)`
- `findById(long id)`
- `save(Child child)`

---

### EventRepository

Operatii:
- `findById(long id)`
- `findAll()`

---

### RegistrationRepository

Operatii:
- `findByChildId(long childId)`
- `findByEventId(long eventId)`
- `save(Registration registration)`
- `delete(Registration registration)`

---

### UserRepository

Operatii:
- `findByUsername(String username)`

---

# Stadiul implementarii

In acest moment sunt implementate:

- entitatile domeniului (`model`)
- interfetele pentru repository (`repository`)

Urmeaza sa fie implementate:

- repository-uri JDBC
- conectarea la baza de date SQLite
- serviciile aplicatiei
- interfata grafica
## Implementare gRPC - Java (Server)

Serverul este implementat in Java si foloseste gRPC pentru a expune functionalitatea aplicatiei.

Fisierul `contest.proto` defineste contractul de comunicare (mesaje si metode RPC). Acesta se afla in:

common/src/main/proto/contest.proto

Pe baza acestuia, Gradle genereaza automat clasele Java necesare (stub-uri gRPC), folosind pluginul:

com.google.protobuf

Clasele generate sunt folosite in implementarea serviciului:

ro.mpp2026.server.ContestGrpcService

Aceasta clasa extinde:

ContestRpcGrpc.ContestRpcImplBase

si implementeaza metodele definite in `.proto`, apelând logica existenta din `ContestService`.

Serverul este pornit din clasa:

ro.mpp2026.server.StartGrpcServer

si ruleaza pe portul:

55556

Pentru notificari in timp real se foloseste streaming gRPC (server → client), prin metoda:

SubscribeUpdates

Serverul pastreaza lista clientilor conectati si trimite notificari atunci cand apar modificari (ex: inscriere copil).
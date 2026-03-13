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
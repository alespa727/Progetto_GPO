# 🎮 Community Gaming Platform

> Piattaforma web per community di gamers con messaggistica in tempo reale, canali tematici e chat private.

---

## 📋 Descrizione

Sviluppata con React (frontend), Spring Boot (backend REST), Node.js (WebSocket) e MySQL (database). Offre autenticazione sicura tramite JWT, gestione di community con sezioni e canali, sistema di amicizie e supporto agli allegati.

---

## 🏗️ Architettura

Il sistema è composto da quattro componenti principali che comunicano tra loro:

- **React Client** comunica con Spring Boot tramite REST API e con Node.js tramite WebSocket.
- **Spring Boot** gestisce la logica applicativa, l'autenticazione JWT e l'accesso al database.
- **Node.js** gestisce la messaggistica in tempo reale tramite Socket.io.
- **MySQL** è il database condiviso da entrambi i backend.

| Componente | Tecnologie | Ruolo |
|---|---|---|
| React Client | Vite, React Router, Axios, Socket.io-client | Interfaccia utente |
| Spring Boot | REST API, Spring Security, JWT, JPA | Backend applicativo |
| Node.js | Socket.io, Express, jsonwebtoken | Messaggistica real-time |
| MySQL | — | Persistenza dei dati |

**Comunicazioni:**
- React ↔ Spring Boot: HTTP REST (JSON)
- React ↔ Node.js: WebSocket (Socket.io)
- Spring Boot → MySQL: JPA / Hibernate
- Node.js → MySQL: mysql2
- 
---

## 🛠️ Stack Tecnologico

### Frontend
| Tecnologia | Versione | Utilizzo |
|---|---|---|
| React | 18.x | Framework UI |
| React Router DOM | 6.x | Routing SPA |
| Axios | 1.x | Chiamate HTTP |
| Socket.io-client | 4.x | WebSocket |
| TailwindCSS | 3.x | Stile e layout |
| Vite | 5.x | Build tool |

### Backend REST (Spring Boot)
| Tecnologia | Utilizzo |
|---|---|
| Spring Boot | Framework principale |
| Spring Security + JWT | Autenticazione stateless |
| Spring Data JPA / Hibernate | ORM e accesso DB |
| Lombok | Riduzione boilerplate |
| ModelMapper | Conversione entità ↔ DTO |

### Backend WebSocket (Node.js)
| Tecnologia | Utilizzo |
|---|---|
| Node.js | Runtime server |
| Socket.io | Gestione WebSocket |
| Express.js | Server HTTP base |
| jsonwebtoken | Verifica JWT |
| mysql2 | Connessione MySQL |

### Database
| Tecnologia | Utilizzo |
|---|---|
| MySQL | Database relazionale |

---

## 🗄️ Schema del Database

```
users ──────────────────────────────────────────────────────────┐
  │                                                              │
  ├──► communities (fkUserOwner)                                 │
  │         └──► registrations (fkCommunity, fkUser) ◄──────────┤
  │         └──► sections                                        │
  │                   └──► channels                              │
  │                             └──► messagesCommunity ◄────────┤
  │                                        └──► attachmentsCommunity
  │                                                              │
  ├──► friendships (fkUser1, fkUser2) ◄──────────────────────────┘
  │         └──► chats
  │                 ├──► messagesChat ◄──────────────────────────┐
  │                 │         └──► attachmentsChat               │
  │                 └──► callsChat                               │
  └──────────────────────────────────────────────────────────────┘
```

### Tabelle principali

| Tabella | Descrizione |
|---|---|
| `users` | Utenti della piattaforma |
| `communities` | Community create dagli utenti |
| `registrations` | Iscrizioni utenti alle community |
| `sections` | Sezioni interne a una community |
| `channels` | Canali all'interno delle sezioni |
| `friendships` | Relazioni di amicizia tra utenti |
| `chats` | Chat private legate a un'amicizia |
| `messagesChat` | Messaggi nelle chat private |
| `messagesCommunity` | Messaggi nei canali community |
| `attachmentsChat` | Allegati nei messaggi privati |
| `attachmentsCommunity` | Allegati nei canali |
| `callsChat` | Chiamate tra utenti |

---

## 🔐 Autenticazione

L'autenticazione è gestita tramite **JWT (JSON Web Token)**

---

## ⚡ WebSocket — Flusso messaggi

```
Client                     Node.js Server               MySQL
  │                              │                         │
  │──── connect (JWT) ──────────►│                         │
  │◄─── auth ok / room join ─────│                         │
  │                              │                         │
  │──── sendMessage ────────────►│                         │
  │                              │──── INSERT message ────►│
  │                              │◄─── ok ─────────────────│
  │◄─── receiveMessage ──────────│ (broadcast alla room)   │
  │                              │                         │
  │──── disconnect ─────────────►│                         │
  │                              │ (leave room, notify)    │
```

---

## 👥 Team

| Nome | Ruolo |
|---|---|
| **Spartano Alessio** | Project Manager / Backend Spring Boot / Frontend React |
| **Ferrarese Tommaso** | Backend Spring Boot  |
| **Maiolino Artale Samuele** |   Landing Page |

---

## 📁 Struttura del Progetto

```
/
├── frontend/                  # React + Vite
│   └── src/
│       ├── components/        # Componenti riutilizzabili
│       ├── pages/             # Pagine (Login, Chat, Dashboard...)
│       ├── context/           # React Context (auth, tema)
│       ├── services/          # Chiamate API REST
│       └── socket/            # Connessione WebSocket
│
├── backend-spring/            # Spring Boot
│   └── src/main/java/
│       ├── controller/        # REST Controllers
│       ├── service/           # Business logic
│       ├── repository/        # JPA Repositories
│       ├── entity/            # Entità JPA
│       └── security/          # JWT + Spring Security
│
├── backend-ws/                # Node.js WebSocket
│   ├── handlers/              # messageHandler, roomHandler
│   ├── middleware/            # auth JWT
│   └── index.js
│
└── database/
    └── schema.sql             # Schema MySQL
```

---

*Anno scolastico 2025-2026 — GPO (Gestione Progetto, Organizzazione d'Impresa)*

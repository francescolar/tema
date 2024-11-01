# TeMa | Termo Manutenzioni

# Progetto: Sistema di Gestione Manutenzione

## Panoramica

Il **Sistema di Gestione Manutenzione** è un'applicazione sviluppata con **Spring Boot** per il backend e **Vue.js** per
il frontend, che permette di gestire utenti, sedi, impianti e operazioni di manutenzione. Offre una serie di filtri
avanzati, gestione sicura degli utenti.

### Caratteristiche principali:

- **Gestione Utenti**: Creazione, modifica, filtro e eliminazione di utenti, con crittografia delle password e invio di
  email per notifiche.
- **Gestione Sedi e Impianti**: Operazioni CRUD per sedi e impianti, con aggiornamenti automatici su numero di impianti
  e ore di lavoro.
- **Monitoraggio Manutenzioni**: Traccia le operazioni di manutenzione, le ore impiegate, e fornisce descrizioni
  dettagliate. Inclusi filtri avanzati per operazioni basati su data, utente, impianto e ore di lavoro.
- **Form di Richiesta Assistenza**: Un form dedicato per gli utenti per richiedere assistenza.
- **Audit Log**: Registrazione delle operazioni di eliminazione e tracciamento delle ore di lavoro a livello di utente,
  impianto e sede.
- **Validazioni**: Le validazioni sono effettuate su tre livelli: frontend, backend e database, per garantire
  l'integrità dei dati in tutte le fasi.
- **Email Integration**: Sistema di invio email per notifiche e conferme di azioni critiche.

### Tecnologia Utilizzata:

- **Backend**: Spring Boot, Spring Security, PostgreSQL, BCrypt.
- **Frontend**: Vue.js, Thymeleaf, Bootstrap per il design responsivo.
- **Build Tools**: Gradle per la gestione delle dipendenze.
- **Google Places API**: Integrato per autocompletare indirizzi.
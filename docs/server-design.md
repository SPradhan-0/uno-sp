# Server-Side Design

## Purpose

The server owns the authoritative UNO game state and exposes it through a REST API. A client should ask the server to create games, add players, play cards, draw cards, call UNO, and fetch the current game state.

## Technology

- Java
- Spring Boot
- REST controllers
- Service layer for game rules
- DTOs/records for request and response bodies

## Main Responsibilities

- Create and store game sessions.
- Track players, hands, draw pile, discard pile, current turn, direction, and winner.
- Validate player actions before changing game state.
- Return updated game state to the client after every move.

## REST Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| `POST` | `/api/games` | Create a new game |
| `GET` | `/api/games/{gameId}` | Get the current game state |
| `POST` | `/api/games/{gameId}/players` | Add a player |
| `POST` | `/api/games/{gameId}/play-card` | Play a card from a player's hand |
| `POST` | `/api/games/{gameId}/draw-card` | Draw a card |
| `POST` | `/api/games/{gameId}/call-uno` | Call UNO |

## Relationship To Existing Code

The current desktop game remains unchanged. This server is an added architecture layer that demonstrates how the same UNO concept could be exposed over HTTP for a client-server version.


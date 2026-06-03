# Client-Side Design

## Purpose

The client displays the UNO game and communicates with the Spring Boot server using REST API calls. The existing desktop game could later be adapted to use this API, or a new web/mobile client could be built.

## Main Responsibilities

- Display cards, players, current turn, discard pile, and winner.
- Send user actions to the server.
- Refresh the visible game state after each server response.
- Show errors when the server rejects an invalid move.

## Example Client Flow

1. Client calls `POST /api/games` to create a game.
2. Client calls `POST /api/games/{gameId}/players` to add players.
3. Client calls `GET /api/games/{gameId}` to show the current state.
4. When a player clicks a card, client calls `POST /api/games/{gameId}/play-card`.
5. If a player has no playable card, client calls `POST /api/games/{gameId}/draw-card`.
6. When a player reaches one card, client calls `POST /api/games/{gameId}/call-uno`.

## Interoperability Goal

The client and server interoperate when the client can call every REST endpoint, receive JSON responses, and update the displayed game state based on server data.


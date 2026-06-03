# Interoperability Test Plan

## Goal

Verify that the client can communicate with the Spring Boot REST server and that the server returns usable JSON game state.

## Automated Test

The file `spring-server/src/test/java/com/example/uno/UnoRestInteroperabilityTest.java` starts the Spring Boot server on a random port and uses `TestRestTemplate` as a client.

It checks that a client can:

- Create a game.
- Add a human player.
- Add a bot player.
- Call UNO.
- Fetch the current game state.
- Confirm each player has seven cards.

## Manual Test Commands

From the `spring-server` folder:

```sh
mvn spring-boot:run
```

Then in another terminal:

```sh
curl -X POST http://localhost:8080/api/games
```

Use the returned `gameId` in later calls:

```sh
curl -X POST http://localhost:8080/api/games/GAME_ID/players \
  -H "Content-Type: application/json" \
  -d '{"name":"Human"}'

curl http://localhost:8080/api/games/GAME_ID
```

## Current Scope

This is an added REST architecture prototype. The original desktop UNO game still runs through `run.sh` and has not been converted to call the REST server yet.


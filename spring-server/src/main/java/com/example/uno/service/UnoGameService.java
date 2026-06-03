package com.example.uno.service;

import com.example.uno.model.GameStateDto;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UnoGameService {
    private final Map<String, UnoGameSession> games = new ConcurrentHashMap<>();

    public GameStateDto createGame() {
        UnoGameSession game = new UnoGameSession();
        games.put(game.id(), game);
        return game.state();
    }

    public GameStateDto getGame(String gameId) {
        return findGame(gameId).state();
    }

    public GameStateDto addPlayer(String gameId, String name) {
        return findGame(gameId).addPlayer(name);
    }

    public GameStateDto playCard(String gameId, String playerName, int cardIndex, String chosenColor) {
        return findGame(gameId).playCard(playerName, cardIndex, chosenColor);
    }

    public GameStateDto drawCard(String gameId, String playerName) {
        return findGame(gameId).drawCard(playerName);
    }

    public GameStateDto callUno(String gameId, String playerName) {
        return findGame(gameId).callUno(playerName);
    }

    private UnoGameSession findGame(String gameId) {
        UnoGameSession game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Unknown game: " + gameId);
        }
        return game;
    }
}


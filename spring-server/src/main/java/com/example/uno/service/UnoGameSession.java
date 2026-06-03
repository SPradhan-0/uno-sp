package com.example.uno.service;

import com.example.uno.model.CardDto;
import com.example.uno.model.GameStateDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class UnoGameSession {
    private final String id = UUID.randomUUID().toString();
    private final List<String> players = new ArrayList<>();
    private final Map<String, List<CardDto>> hands = new HashMap<>();
    private final Map<String, Boolean> unoCalled = new HashMap<>();
    private final List<CardDto> drawPile = new ArrayList<>();
    private final List<CardDto> discardPile = new ArrayList<>();
    private int currentPlayerIndex;
    private int direction = 1;
    private String winner;

    UnoGameSession() {
        buildDeck();
        Collections.shuffle(drawPile);
        discardPile.add(drawPile.remove(drawPile.size() - 1));
    }

    String id() {
        return id;
    }

    GameStateDto state() {
        return new GameStateDto(
                id,
                List.copyOf(players),
                copyHands(),
                discardPile.get(discardPile.size() - 1),
                currentPlayerIndex,
                players.isEmpty() ? null : players.get(currentPlayerIndex),
                direction == 1 ? "clockwise" : "counterclockwise",
                winner,
                Map.copyOf(unoCalled));
    }

    GameStateDto addPlayer(String name) {
        requireNotBlank(name, "Player name is required");
        if (hands.containsKey(name)) {
            throw new IllegalArgumentException("Player already exists: " + name);
        }
        players.add(name);
        hands.put(name, new ArrayList<>());
        unoCalled.put(name, false);
        for (int i = 0; i < 7; i++) {
            draw(name);
        }
        return state();
    }

    GameStateDto drawCard(String playerName) {
        requireCurrentPlayer(playerName);
        draw(playerName);
        advanceTurn();
        return state();
    }

    GameStateDto callUno(String playerName) {
        requireKnownPlayer(playerName);
        unoCalled.put(playerName, true);
        return state();
    }

    GameStateDto playCard(String playerName, int cardIndex, String chosenColor) {
        requireCurrentPlayer(playerName);
        List<CardDto> hand = hands.get(playerName);
        if (cardIndex < 0 || cardIndex >= hand.size()) {
            throw new IllegalArgumentException("Card index is out of range");
        }

        CardDto selected = hand.get(cardIndex);
        CardDto top = discardPile.get(discardPile.size() - 1);
        if (!isCompatible(selected, top)) {
            throw new IllegalArgumentException("Selected card is not playable");
        }

        hand.remove(cardIndex);
        CardDto played = normalizeWildColor(selected, chosenColor);
        discardPile.add(played);
        applyActionEffect(played);

        if (hand.isEmpty()) {
            winner = playerName;
        } else if (hand.size() == 1 && !Boolean.TRUE.equals(unoCalled.get(playerName))) {
            draw(playerName);
            draw(playerName);
        }

        unoCalled.put(playerName, false);
        if (winner == null) {
            advanceTurn();
        }
        return state();
    }

    private void buildDeck() {
        String[] colors = {"RED", "GREEN", "BLUE", "YELLOW"};
        for (String color : colors) {
            drawPile.add(new CardDto(color, "0"));
            for (int number = 1; number <= 9; number++) {
                drawPile.add(new CardDto(color, String.valueOf(number)));
                drawPile.add(new CardDto(color, String.valueOf(number)));
            }
            for (int i = 0; i < 2; i++) {
                drawPile.add(new CardDto(color, "SKIP"));
                drawPile.add(new CardDto(color, "REVERSE"));
                drawPile.add(new CardDto(color, "PLUS2"));
            }
        }
        for (int i = 0; i < 4; i++) {
            drawPile.add(new CardDto("BLACK", "WILD"));
            drawPile.add(new CardDto("BLACK", "PLUS4"));
        }
    }

    private void draw(String playerName) {
        if (drawPile.isEmpty()) {
            CardDto top = discardPile.remove(discardPile.size() - 1);
            drawPile.addAll(discardPile);
            discardPile.clear();
            discardPile.add(top);
            Collections.shuffle(drawPile);
        }
        hands.get(playerName).add(drawPile.remove(drawPile.size() - 1));
    }

    private boolean isCompatible(CardDto selected, CardDto top) {
        return selected.color().equals("BLACK")
                || selected.color().equals(top.color())
                || selected.symbol().equals(top.symbol());
    }

    private CardDto normalizeWildColor(CardDto selected, String chosenColor) {
        if (!selected.color().equals("BLACK")) {
            return selected;
        }
        requireNotBlank(chosenColor, "A wild card requires a chosen color");
        return new CardDto(chosenColor.toUpperCase(), selected.symbol());
    }

    private void applyActionEffect(CardDto played) {
        switch (played.symbol()) {
            case "REVERSE" -> direction *= -1;
            case "SKIP" -> advanceTurn();
            case "PLUS2" -> {
                advanceTurn();
                draw(players.get(currentPlayerIndex));
                draw(players.get(currentPlayerIndex));
            }
            case "PLUS4" -> {
                advanceTurn();
                String nextPlayer = players.get(currentPlayerIndex);
                for (int i = 0; i < 4; i++) {
                    draw(nextPlayer);
                }
            }
            default -> {
            }
        }
    }

    private void advanceTurn() {
        if (players.isEmpty()) {
            return;
        }
        currentPlayerIndex = Math.floorMod(currentPlayerIndex + direction, players.size());
    }

    private void requireCurrentPlayer(String playerName) {
        requireKnownPlayer(playerName);
        if (!players.get(currentPlayerIndex).equals(playerName)) {
            throw new IllegalArgumentException("It is not " + playerName + "'s turn");
        }
    }

    private void requireKnownPlayer(String playerName) {
        requireNotBlank(playerName, "Player name is required");
        if (!hands.containsKey(playerName)) {
            throw new IllegalArgumentException("Unknown player: " + playerName);
        }
    }

    private void requireNotBlank(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    private Map<String, List<CardDto>> copyHands() {
        Map<String, List<CardDto>> copy = new HashMap<>();
        for (Map.Entry<String, List<CardDto>> entry : hands.entrySet()) {
            copy.put(entry.getKey(), List.copyOf(entry.getValue()));
        }
        return copy;
    }
}


package com.example.uno.model;

import java.util.List;
import java.util.Map;

public record GameStateDto(
        String gameId,
        List<String> players,
        Map<String, List<CardDto>> hands,
        CardDto discardTop,
        int currentPlayerIndex,
        String currentPlayer,
        String direction,
        String winner,
        Map<String, Boolean> unoCalled) {
}


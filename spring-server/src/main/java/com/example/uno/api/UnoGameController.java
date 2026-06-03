package com.example.uno.api;

import com.example.uno.model.AddPlayerRequest;
import com.example.uno.model.GameStateDto;
import com.example.uno.model.PlayCardRequest;
import com.example.uno.model.PlayerActionRequest;
import com.example.uno.service.UnoGameService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/games")
public class UnoGameController {
    private final UnoGameService service;

    public UnoGameController(UnoGameService service) {
        this.service = service;
    }

    @PostMapping
    public GameStateDto createGame() {
        return service.createGame();
    }

    @GetMapping("/{gameId}")
    public GameStateDto getGame(@PathVariable String gameId) {
        return service.getGame(gameId);
    }

    @PostMapping("/{gameId}/players")
    public GameStateDto addPlayer(@PathVariable String gameId, @RequestBody AddPlayerRequest request) {
        return service.addPlayer(gameId, request.name());
    }

    @PostMapping("/{gameId}/play-card")
    public GameStateDto playCard(@PathVariable String gameId, @RequestBody PlayCardRequest request) {
        return service.playCard(gameId, request.playerName(), request.cardIndex(), request.chosenColor());
    }

    @PostMapping("/{gameId}/draw-card")
    public GameStateDto drawCard(@PathVariable String gameId, @RequestBody PlayerActionRequest request) {
        return service.drawCard(gameId, request.playerName());
    }

    @PostMapping("/{gameId}/call-uno")
    public GameStateDto callUno(@PathVariable String gameId, @RequestBody PlayerActionRequest request) {
        return service.callUno(gameId, request.playerName());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException exception) {
        return Map.of("error", exception.getMessage());
    }
}


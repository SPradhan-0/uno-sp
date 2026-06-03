package com.example.uno;

import com.example.uno.model.AddPlayerRequest;
import com.example.uno.model.GameStateDto;
import com.example.uno.model.PlayerActionRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnoRestInteroperabilityTest {
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void clientCanCreateGameAddPlayersAndFetchState() {
        String baseUrl = "http://localhost:" + port + "/api/games";

        GameStateDto created = restTemplate.postForObject(baseUrl, null, GameStateDto.class);
        assertThat(created.gameId()).isNotBlank();

        GameStateDto withHuman = restTemplate.postForObject(
                baseUrl + "/" + created.gameId() + "/players",
                new AddPlayerRequest("Human"),
                GameStateDto.class);
        assertThat(withHuman.players()).containsExactly("Human");

        GameStateDto withBot = restTemplate.postForObject(
                baseUrl + "/" + created.gameId() + "/players",
                new AddPlayerRequest("Bot1"),
                GameStateDto.class);
        assertThat(withBot.players()).containsExactly("Human", "Bot1");

        GameStateDto afterUnoCall = restTemplate.postForObject(
                baseUrl + "/" + created.gameId() + "/call-uno",
                new PlayerActionRequest("Human"),
                GameStateDto.class);
        assertThat(afterUnoCall.unoCalled().get("Human")).isTrue();

        GameStateDto fetched = restTemplate.getForObject(
                baseUrl + "/" + created.gameId(),
                GameStateDto.class);
        assertThat(fetched.gameId()).isEqualTo(created.gameId());
        assertThat(fetched.hands().get("Human")).hasSize(7);
        assertThat(fetched.hands().get("Bot1")).hasSize(7);
    }
}


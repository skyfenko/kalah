package com.skyfenko.web.controller;

import com.skyfenko.service.dto.GameRequest;
import com.skyfenko.service.dto.GameResponse;
import com.skyfenko.service.rules.orchestrator.RulesOrchestrator;
import com.skyfenko.web.constants.URIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Main entry point for all the manipulations around the game: make a move, get info about intermediate state of the game
 * and restart the game
 *
 * @author Stanislav Kyfenko
 */
@RestController
public class KalahGameController {

    private final RulesOrchestrator rulesOrchestrator;

    @Autowired
    public KalahGameController(RulesOrchestrator rulesOrchestrator) {
        this.rulesOrchestrator = rulesOrchestrator;
    }

    @PostMapping(value = URIConstants.Api.TURNS)
    public ResponseEntity<GameResponse> makeTurn(@RequestBody GameRequest gameRequest) {
        return ResponseEntity.accepted().body(rulesOrchestrator.orchestrate(gameRequest));
    }

    @GetMapping(value = URIConstants.Api.GAME_INFO)
    public ResponseEntity<GameResponse> getGameInfo() {
        return ResponseEntity.ok(rulesOrchestrator.gameInfo());
    }

    @PostMapping(value = URIConstants.Api.RESTART_GAME)
    public ResponseEntity<GameResponse> restartGame() {
        return ResponseEntity.created(URI.create(URIConstants.Api.GAME_INFO)).body(rulesOrchestrator.restartGame());
    }

}

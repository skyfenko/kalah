package com.skyfenko.service.rules.orchestrator.impl;

import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import com.skyfenko.domain.turn.Turn;
import com.skyfenko.service.dto.BoardDTO;
import com.skyfenko.service.dto.GameRequest;
import com.skyfenko.service.dto.GameResponse;
import com.skyfenko.service.rules.chain.RulesApplier;
import com.skyfenko.service.rules.orchestrator.RulesOrchestrator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Coordinate all the flows of Kalah and monitor rules to be applied for each turn and each player
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class RulesOrchestratorImpl implements RulesOrchestrator {

    private final List<RulesApplier> rulesAppliers;
    private Game game;

    @Autowired
    public RulesOrchestratorImpl(List<RulesApplier> rulesAppliers) {
        this.rulesAppliers = rulesAppliers;
        this.game = initGame();
    }

    private Game initGame() {
        Game game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        //let south to start
        game.setTakeTurnPlayer(game.getPlayers().get(0));
        return game;
    }

    @Override
    public GameResponse orchestrate(GameRequest gameRequest) {
        log.debug("received request {}. fill Game with these attributes", gameRequest);
        fillGameWithRequest(game, gameRequest);

        log.debug("start applying rules to new turn");
        rulesAppliers.forEach(applier -> applier.applyRule(game));
        log.debug("applying rules was finished. converting to representation object");

        GameResponse response = createResponse();
        log.debug("sending response {}" , response);
        return response;
    }

    @Override
    public GameResponse gameInfo() {
        return createResponse();
    }

    @Override
    public GameResponse restartGame() {
        this.game = initGame();
        return createResponse();
    }

    /**
     * Convert {@linkplain Game} attributes to {@linkplain GameResponse} for web
     *
     * @return {@linkplain GameResponse}
     */
    private GameResponse createResponse() {
        BoardDTO board = new BoardDTO();
        board.setHouses(game.getBoard().getHouses());
        
        return GameResponse.builder().board(board)
                .over(game.isOver())
                .started(game.isStarted())
                .winner(game.getWinner())
                .players(game.getPlayers().stream().map(Player::name).collect(Collectors.toList()))
                .takeTurnPlayer(game.getTakeTurnPlayer().name())
                .build();
    }

    /**
     * Fill {@linkplain Game} with attributes received from web through {@linkplain GameRequest}
     *
     * @param game        {@linkplain Game}
     * @param gameRequest {@linkplain GameRequest}
     */
    private void fillGameWithRequest(Game game, GameRequest gameRequest) {
        Player newTakeTurnPlayer = game.getPlayers().stream()
                .filter(player -> player.name().equals(gameRequest.getTakeTurnPlayer()))
                .collect(Collectors.toList())
                .get(0);

        game.setTakeTurnPlayer(newTakeTurnPlayer);
        game.setCurrentTurn(new Turn(gameRequest.getCurrentTurn()));
    }
}

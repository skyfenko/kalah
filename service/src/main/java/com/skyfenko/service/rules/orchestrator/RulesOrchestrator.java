package com.skyfenko.service.rules.orchestrator;

import com.skyfenko.service.dto.GameRequest;
import com.skyfenko.service.dto.GameResponse;

/**
 * Coordinate all the flows of Kalah and monitor rules to be applied for each turn and each player
 *
 * @author Stanislav Kyfenko
 */
public interface RulesOrchestrator {

    /**
     * Loop through all the {@linkplain com.skyfenko.service.rules.chain.RulesApplier} and apply all the appropriate rules to the current turn
     *
     * @param gameRequest {@linkplain GameRequest} - data about take-turn-player and current turn
     * @return {@linkplain GameResponse}
     */
    GameResponse orchestrate(GameRequest gameRequest);

    /**
     * Short overview of all the current game details (board, players, etc.)
     *
     * @return {@linkplain GameResponse}
     */
    GameResponse gameInfo();

    /**
     * Re-initiate {@linkplain com.skyfenko.domain.game.Game} to be able to play again
     *
     * @return {@linkplain GameResponse}
     */
    GameResponse restartGame();

}

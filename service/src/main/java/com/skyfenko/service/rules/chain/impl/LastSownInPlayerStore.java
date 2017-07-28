package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.game.Game;
import com.skyfenko.service.rules.chain.RulesApplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * If the last sown stone lands in the player's store, the player gets an additional move.
 * There is no limit on the number of moves a player can make in their turn.
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class LastSownInPlayerStore implements RulesApplier {

    /**
     * Leave take-turn-player as-is and set current turn to null to skip next steps if last stone lands in the store
     *
     * @param game {@link Game}
     */
    @Override
    public void applyRule(Game game) {
        if (!game.isOver()) {
            int lastSownIndex = game.getCurrentTurn().getLastSownIndex();

            if (game.getTakeTurnPlayer().storeIndex() == lastSownIndex) {
                log.debug("Last sown stone is located at player's store. Setting additional move.");
                // leave take-turn-player the same so that additional move is guaranteed
                game.setPreviousTurn(game.getCurrentTurn());
                game.setCurrentTurn(null);
                // finishing with current turn. remaining the same player to make additional move
            }
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.service.rules.chain.RulesApplier;

/**
 * Check whether player can take turn with the chosen house.
 * It could be the case when chosen house is owned by another player, so such a move is prohibited.
 *
 * @author Stanislav Kyfenko
 */
abstract class CheckTakeTurnPlayerRulesApplier implements RulesApplier {

    /**
     * Check whether current user can take turn and choose the house with {@param chosenHouse}.
     * If house is not owned by current player, throw exception
     *
     * @param game        {@link Game}
     * @param chosenHouse index of house the player has chosen
     */
    boolean canPlayerTakeTurn(Game game, int chosenHouse) {
        int storeIndex = game.getTakeTurnPlayer().storeIndex();

        return chosenHouse < storeIndex && chosenHouse >= storeIndex - Board.HOUSES_PER_PLAYER;

    }
}

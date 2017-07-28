package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Players take turns sowing their stones.
 * On a turn, the player removes all stones from one of the houses under their control.
 * Moving counter-clockwise, the player drops one stone in each house in turn, including the player's own store
 * but not their opponent's.
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class SowStones extends CheckTakeTurnPlayerRulesApplier {

    /**
     * If the game is not over and player can move from chosen house, sow the stones from that house anti-clockwise.
     *
     * @param game {@link Game}
     */
    @Override
    public void applyRule(Game game) {
        if (!game.isOver()) {
            int chosenHouse = game.getCurrentTurn().getChosenHouse();
            if (canPlayerTakeTurn(game, chosenHouse)) {
                sow(game, chosenHouse, opponentStoreIndex(game));
            }
        }
    }

    /**
     * Find index of the store of another player
     *
     * @param game {@link Game}
     * @return store index of another player, otherwise throw exception
     */
    private Integer opponentStoreIndex(Game game) {
        return game.getPlayers().stream()
                .map(Player::storeIndex)
                .filter(index -> game.getTakeTurnPlayer().storeIndex() != index)
                .collect(Collectors.toList())
                .get(0);
    }

    /**
     * Pull stones from {@param chosenHouse} and sow them anti-clockwise skipping opponent store
     * If number of stones in {@param chosenHouse} is too big so you can have 2 laps of sowing,
     * {@param chosenHouse} will be sown too on each lap after first one.
     * <p>
     * After sowing is finished, last sown index is kept for further steps of processing based on Kalah rules
     *
     * @param game               {@link Game}
     * @param chosenHouse        index of the house, which has been chosen by current player
     * @param opponentStoreIndex index of store cell of another player. needed to skip it while sowing
     */
    private void sow(Game game, int chosenHouse, Integer opponentStoreIndex) {
        int[] targetArray = game.getBoard().getHouses();

        // start sowing from next house
        int startPositionToMove = chosenHouse + 1;

        // move for the same number of houses as we have stones in house
        int stonesInHouse = targetArray[chosenHouse];

        // pull all the stones from the house
        targetArray[chosenHouse] = 0;

        // keep in mind where last sown stone is located for further steps
        int lastSownIndex = -1;
        // iterate in circular manner over array and put 1 stone into each next house
        for (int i = 0; i < stonesInHouse; i++) {
            lastSownIndex = (i + startPositionToMove) % targetArray.length;

            // skip opponent store and fill next house
            if (lastSownIndex == opponentStoreIndex) {
                lastSownIndex = (++i + startPositionToMove) % targetArray.length;
                //add 1 stone to avoid skipping one cell when we are jumping over opposite player's store
                stonesInHouse += 1;
            }
            targetArray[lastSownIndex] += 1;
        }

        log.debug("Board intermediate state after sowing {}", Arrays.toString(game.getBoard().getHouses()));

        game.getCurrentTurn().setLastSownIndex(lastSownIndex);
    }

    @Override
    public int getOrder() {
        return 1;
    }
}

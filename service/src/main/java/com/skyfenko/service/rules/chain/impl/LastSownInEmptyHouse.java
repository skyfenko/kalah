package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * If the last sown stone lands in an empty house owned by the player, and the opposite house contains seeds,
 * both the last seed and the opposite seeds are captured and placed into the player's store.
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class LastSownInEmptyHouse extends CheckTakeTurnPlayerRulesApplier {

    /**
     * If the game is not over and current turn is not empty (needed to check whether a chain breaks processing on last step),
     * try to capture stones from houses of another player if our last stone lands in our empty house.
     * Otherwise, another player takes a turn.
     *
     * @param game {@link Game}
     */
    @Override
    public void applyRule(Game game) {
        if (game.getCurrentTurn() != null && !game.isOver()) {
            int lastSownIndex = game.getCurrentTurn().getLastSownIndex();


            // if player cannot capture stones, skip capturing and move a turn to another player
            if (!canPlayerTakeTurn(game, lastSownIndex)) {
                log.debug("current player cannot capture stones from opposite house");
                wrapTurnUp(game);
                return;
            }

            captureOppositeAndCurrentHouseIntoPlayerStore(game, lastSownIndex);
            wrapTurnUp(game);
        }
    }

    /**
     * If {@param lastSownIndex} is not located at one of the stores, skipping capturing.
     * If value of house by {@param lastSownIndex} is equal to 1, it means the house was empty before we put last stone there
     * If opposite house contains stones, we can capture them with stones from our house and put into the store
     *
     * @param game          {@link Game}
     * @param lastSownIndex index of house where the last house we put
     */
    private void captureOppositeAndCurrentHouseIntoPlayerStore(Game game, int lastSownIndex) {
        List<Integer> storeIndexes = game.getPlayers().stream().map(Player::storeIndex).collect(Collectors.toList());

        if (!storeIndexes.contains(lastSownIndex) && lastSownHouseIsCurrentPlayerHouse(lastSownIndex, game.getTakeTurnPlayer())) {
            log.debug("last sown element is NOT located at one of the stores");

            int lastSownHouseValue = game.getBoard().getByIndex(lastSownIndex);

            if (lastSownHouseValue == 1) { // it means the house was empty before we put last stone there
                int oppositeHouseIndex = Math.abs(lastSownIndex - 12);
                int oppositeHouseValue = game.getBoard().getByIndex(oppositeHouseIndex);
                if (oppositeHouseValue > 0) {
                    int[] houses = game.getBoard().getHouses();
                    //capturing to the player's store and zeroing opposite cell and own cell
                    houses[game.getTakeTurnPlayer().storeIndex()] += oppositeHouseValue + lastSownHouseValue;
                    houses[oppositeHouseIndex] = 0;
                    houses[lastSownIndex] = 0;
                }
            }
        }
    }

    /**
     * Check whether last sown stone lands in the current player's store
     *
     * @param lastSownIndex  index of cell whether the last stone lands
     * @param takeTurnPlayer {@linkplain Player} current player
     * @return true if {@param lastSownIndex} is one of the current player's houses, false otherwise
     */
    private boolean lastSownHouseIsCurrentPlayerHouse(int lastSownIndex, Player takeTurnPlayer) {
        int storeIndex = takeTurnPlayer.storeIndex();
        return lastSownIndex < storeIndex && lastSownIndex >= storeIndex - Board.HOUSES_PER_PLAYER;
    }

    /**
     * Prepare for next turn. Set current turn as previous one. Find another player to take a turn.
     *
     * @param game {@link Game}
     */
    private void wrapTurnUp(Game game) {
        game.setPreviousTurn(game.getCurrentTurn());
        game.setCurrentTurn(null);
        game.setTakeTurnPlayer(findNextTakeTurnPlayer(game));
    }

    /**
     * Find another player to take a turn
     *
     * @param game {@link Game}
     * @return {@link Player} who will be next
     */
    private Player findNextTakeTurnPlayer(Game game) {
        return game.getPlayers().stream()
                .filter(player -> !player.equals(game.getTakeTurnPlayer()))
                .collect(Collectors.toList())
                .get(0);
    }

    @Override
    public int getOrder() {
        return 3;
    }
}

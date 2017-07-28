package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import com.skyfenko.service.rules.chain.RulesApplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * When one player no longer has any seeds in any of their houses, the game ends.
 * The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class CheckEmptyHousesAndSumResults implements RulesApplier {

    /**
     * Last element in the chain. If game is not over, check whether one of the player has all the empty houses.
     * If so, move other player's stones to his store, count stones in each store and find the winner.
     * Otherwise, proceed the game.
     *
     * @param game {@link Game}
     */
    @Override
    public void applyRule(Game game) {
        if (!game.isOver()) {
            boolean needToFinishTheGame = checkAndMove(game);
            if (needToFinishTheGame) {
                findWinner(game);
            }
        }
    }

    /**
     * Find winner in case of finishing the game.
     * 1. Calculate store value for both players and sort them ascending by store value. The last is a winner.
     * 2. If both players have the same store value, it is decided to be a draw. pretty unlikely case
     * 3. Otherwise, last player in the map is the winner. Set his name to {@link Game} and game is over. No further processing is needed.
     *
     * @param game {@link Game}
     */
    private void findWinner(Game game) {
        log.debug("aggregate player names with values of their stores");
        TreeMap<Integer, String> storeValue2PlayerName = game.getPlayers().stream()
                .collect(Collectors.toMap(player -> game.getBoard().getByIndex(player.storeIndex()),
                        Player::name,
                        (a, b) -> a, // merging function in case of duplicate we accept first
                        TreeMap::new));

        log.debug("result of the game {}", storeValue2PlayerName);

        if (storeValue2PlayerName.size() == 1) {
            // if there is only one entry in map, it means store values for both players are the same. unlikely, but... could be a case
            log.debug("both players have the same result. setting as a draw");
            game.setWinner("DRAW");
            game.setOver(Boolean.TRUE);
        } else {
            String winner = storeValue2PlayerName.pollLastEntry().getValue();
            log.debug("winner is {}. finishing the game" + winner);
            game.setWinner(winner);
            game.setOver(Boolean.TRUE);
        }
    }

    /**
     * Check both players one-by-one whether one of them has all the empty houses on the board.
     * 1. If so, stones from the houses of another player are moved to his store.
     * 2. Next, houses are set up to zero
     * 3. If one has all the empty houses, game should be finished, otherwise, game should proceed.
     *
     * @param game {@link Game}
     * @return true if game should be finished, otherwise false
     */
    private boolean checkAndMove(Game game) {
        log.trace("try to check whether one of the players has all the empty houses");

        boolean needToFinishTheGame = Boolean.FALSE;

        List<Integer> sortedStoreIndexes = game.getPlayers().stream().map(Player::storeIndex).sorted().collect(Collectors.toList());

        Integer southPlayerStoreIndex = sortedStoreIndexes.get(0);
        Integer northPlayerStoreIndex = sortedStoreIndexes.get(1);

        boolean southPlayerFinished = checkWhetherPlayerHasAllHousesEmpty(game, southPlayerStoreIndex);

        log.debug("south player has {} all empty houses", southPlayerFinished ? "" : "not");

        if (southPlayerFinished) {
            moveRemainingStonesToAnotherPlayerStore(game, northPlayerStoreIndex);
            needToFinishTheGame = Boolean.TRUE;
        } else {
            boolean northPlayerFinished = checkWhetherPlayerHasAllHousesEmpty(game, northPlayerStoreIndex);

            log.debug("north player has {} all empty houses", northPlayerFinished ? "" : "not");

            if (northPlayerFinished) {
                moveRemainingStonesToAnotherPlayerStore(game, southPlayerStoreIndex);
                needToFinishTheGame = Boolean.TRUE;
            }
        }
        return needToFinishTheGame;
    }

    /**
     * Move remaining stones from the houses of another player to his store and clean houses up
     *
     * @param game                           {@link Game}
     * @param whoHasNonEmptyHousesStoreIndex index to cell of {@link Board#houses} for store of another player (who doesn't have all the empty houses)
     */
    private void moveRemainingStonesToAnotherPlayerStore(Game game, Integer whoHasNonEmptyHousesStoreIndex) {
        int[] boardHouses = game.getBoard().getHouses();

        log.debug("get houses of another player (who still has non-empty houses)");
        int[] playerHouses = Arrays.copyOfRange(boardHouses, whoHasNonEmptyHousesStoreIndex - Board.HOUSES_PER_PLAYER, whoHasNonEmptyHousesStoreIndex);

        log.debug("count sum of remaining stones in non-empty houses for another user and add them to store");
        int sumOfStonesInNonEmptyHousesForAnotherPlayer = Arrays.stream(playerHouses).sum();
        boardHouses[whoHasNonEmptyHousesStoreIndex] += sumOfStonesInNonEmptyHousesForAnotherPlayer;

        cleanUpHousesAfterSummingThemToStore(whoHasNonEmptyHousesStoreIndex, boardHouses);
    }

    private void cleanUpHousesAfterSummingThemToStore(Integer whoHasNonEmptyHousesStoreIndex, int[] boardHouses) {
        log.debug("erase houses of another player after summing them to the store");
        for (int i = whoHasNonEmptyHousesStoreIndex - Board.HOUSES_PER_PLAYER; i < whoHasNonEmptyHousesStoreIndex; i++) {
            boardHouses[i] = 0;
        }
    }

    /**
     * Find all the houses of the player and check whether the sum of them is 0.
     *
     * @param game       {@link Game}
     * @param storeIndex index of the store cell for player
     * @return true if all the houses are empty
     */
    private boolean checkWhetherPlayerHasAllHousesEmpty(Game game, Integer storeIndex) {
        int[] playerHouses = Arrays.copyOfRange(game.getBoard().getHouses(), storeIndex - Board.HOUSES_PER_PLAYER, storeIndex);
        return Arrays.stream(playerHouses).sum() == 0;
    }

    @Override
    public int getOrder() {
        return 4;
    }
}

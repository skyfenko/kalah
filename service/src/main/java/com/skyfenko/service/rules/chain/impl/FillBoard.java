package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import com.skyfenko.service.rules.chain.RulesApplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.skyfenko.domain.board.Board.STONES_PER_HOUSE;

/**
 * At the beginning of the game, six stones are placed in each house.
 * Filling houses in board with six stones excepting stores owned by each player.
 *
 * @author Stanislav Kyfenko
 */
@Service
@Slf4j
public class FillBoard implements RulesApplier {

    /**
     * If the game is not over, fill the board and prepare the system to start the game
     *
     * @param game {@link Game}
     */
    @Override
    public void applyRule(Game game) {
        if (!game.isOver()) {
            fillBoardWithInitialStoneValues(game, findStoreIndexes(game));
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * Should be called once before the beginning of the game.
     * All the houses of the board are filled by {@link Board#STONES_PER_HOUSE} numbers
     * excepting 2 stores, which should be left empty at the beginning.
     */
    private void fillBoardWithInitialStoneValues(Game game, List<Integer> storeIndexes) {
        Board board = game.getBoard();
        if (!board.isFilled()) {
            log.debug("Board is not filled. Filling all the houses with 6 stones");
            int[] houses = board.getHouses();

            for (int i = 0; i < houses.length; i++) {
                if (!storeIndexes.contains(i)) {
                    houses[i] = STONES_PER_HOUSE;
                }
            }
            board.setFilled(Boolean.TRUE);
            game.setStarted(Boolean.TRUE);
            log.debug("Board was successfully filled");
        }
    }

    /**
     * Find indexes of stores for each player to use it further for having them empty while filling houses
     *
     * @param game {@linkplain Game}
     * @return list of store indexes. size should be equal to number of players
     */
    private List<Integer> findStoreIndexes(Game game) {
        List<Integer> indexes = game.getPlayers().stream().map(Player::storeIndex).collect(Collectors.toList());
        log.debug("Found store indexes {}", indexes);
        return indexes;
    }
}

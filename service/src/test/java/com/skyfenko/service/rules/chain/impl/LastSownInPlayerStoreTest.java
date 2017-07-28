package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.Player;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import com.skyfenko.domain.turn.Turn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * LastSownInPlayerStoreTest
 *
 * @author Stanislav Kyfenko
 */
public class LastSownInPlayerStoreTest {

    private Game game;

    private final FillBoard fillBoard = new FillBoard();
    private final SowStones sowStones = new SowStones();
    private final LastSownInPlayerStore lastSownInPlayerStore = new LastSownInPlayerStore();

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        fillBoard.applyRule(game);
        game.setCurrentTurn(new Turn(3));
        game.setTakeTurnPlayer(new SouthPlayer());
    }

    @Test
    public void testLastSownInTheStore() throws Exception {
        // changing value to 3 to put the last stone into the player store
        game.getBoard().getHouses()[3] = 3;
        sowStones.applyRule(game);
        Turn currentTurn = game.getCurrentTurn();
        Player takeTurnPlayer = game.getTakeTurnPlayer();
        lastSownInPlayerStore.applyRule(game);

        Assert.assertEquals("take-turn-player should be the same when the last stone is put into the player's store",
                takeTurnPlayer, game.getTakeTurnPlayer());
        Assert.assertNull("current turn should be null to force to quit from chain", game.getCurrentTurn());
        Assert.assertEquals("current turn value should be set to previous turn variable", currentTurn, game.getPreviousTurn());
    }

    @Test
    public void testGameIsOver() {
        game.setOver(Boolean.TRUE);

        // changing value to 3 to put the last stone into the player store
        game.getBoard().getHouses()[3] = 3;
        sowStones.applyRule(game);
        Turn currentTurn = game.getCurrentTurn();
        Player takeTurnPlayer = game.getTakeTurnPlayer();
        lastSownInPlayerStore.applyRule(game);

        Assert.assertNotNull("if game is over, current turn should NOT be null", game.getCurrentTurn());
        Assert.assertNotEquals("If game is over, previous turn should NOT be set with current turn", game.getPreviousTurn(), currentTurn);
    }

}
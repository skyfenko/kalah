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
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * LastSownInPlayerStoreTest
 *
 * @author Stanislav Kyfenko
 */
public class LastSownInEmptyHouseTest {

    private Game game;

    private final FillBoard fillBoard = new FillBoard();
    private final SowStones sowStones = new SowStones();
    private final LastSownInPlayerStore lastSownInPlayerStore = new LastSownInPlayerStore();
    private final LastSownInEmptyHouse lastSownInEmptyHouse = new LastSownInEmptyHouse();

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        fillBoard.applyRule(game);
        game.setCurrentTurn(new Turn(3));
        game.setTakeTurnPlayer(new SouthPlayer());
    }

    @Test
    public void testCapturing() throws Exception {
        // changing value to 13 to put the last stone into the house, where we are starting
        int[] houses = game.getBoard().getHouses();
        houses[3] = 13;
        sowStones.applyRule(game);

        lastSownInPlayerStore.applyRule(game);
        Turn futurePreviousTurn = game.getCurrentTurn();
        Player futurePreviousTakeTurnPlayer = game.getTakeTurnPlayer();
        lastSownInEmptyHouse.applyRule(game);

        int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.storeIndex());

        assertEquals("store value should be 9 since we are capturing 1+7 from houses to 1 in the store",
                9, playerStoreValue);

        assertEquals("Another player should take a turn", new NorthPlayer(), game.getTakeTurnPlayer());
        assertEquals(0, houses[3]);
        assertEquals(0, houses[9]);
        assertNull("Current turn should be null", game.getCurrentTurn());
        assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());
    }

    @Test
    public void testPlayerCannotCaptureFromAnotherPlayerHouse() {
        Turn currentTurn = new Turn(1);
        currentTurn.setLastSownIndex(12);
        game.setCurrentTurn(currentTurn);

        lastSownInEmptyHouse.applyRule(game);

        assertEquals("Another player should take a turn", new NorthPlayer(), game.getTakeTurnPlayer());
        assertNull("Current turn should be null", game.getCurrentTurn());
        assertEquals("Current turn should become previous turn", currentTurn, game.getPreviousTurn());
    }

    @Test
    public void testTryingToCaptureWithEmptyOppositeHouse() throws Exception {
        // changing value to 13 to put the last stone into the house, where we are starting
        int[] houses = game.getBoard().getHouses();
        houses[3] = 13;
        sowStones.applyRule(game);

        lastSownInPlayerStore.applyRule(game);
        Turn futurePreviousTurn = game.getCurrentTurn();
        Player futurePreviousTakeTurnPlayer = game.getTakeTurnPlayer();

        // setting opposite house to 0
        houses[9] = 0;

        lastSownInEmptyHouse.applyRule(game);

        int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.storeIndex());

        assertEquals("store value should be 1 since opposite house is empty",
                1, playerStoreValue);

        assertEquals("Another player should take a turn", new NorthPlayer(), game.getTakeTurnPlayer());
        assertEquals(1, houses[3]);
        assertEquals(0, houses[9]);
        assertNull("Current turn should be null", game.getCurrentTurn());
        assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());

    }

    @Test
    public void applyRuleWithHouseNotOwnedByPlayer() throws Exception {
        // changing value to 5 to put the last stone into the house owned by another player
        int[] houses = game.getBoard().getHouses();
        houses[3] = 5;
        houses[8] = 0;
        sowStones.applyRule(game);

        lastSownInPlayerStore.applyRule(game);
        Turn futurePreviousTurn = game.getCurrentTurn();
        Player futurePreviousTakeTurnPlayer = game.getTakeTurnPlayer();
        lastSownInEmptyHouse.applyRule(game);

        int playerStoreValue = game.getBoard().getByIndex(futurePreviousTakeTurnPlayer.storeIndex());

        assertEquals("store value should NOT be increased by capturing",
                1, playerStoreValue);

        assertEquals("Another player should take a turn", new NorthPlayer(), game.getTakeTurnPlayer());
        assertNull("Current turn should be null", game.getCurrentTurn());
        assertEquals("Current turn should become previous turn", futurePreviousTurn, game.getPreviousTurn());
    }
}
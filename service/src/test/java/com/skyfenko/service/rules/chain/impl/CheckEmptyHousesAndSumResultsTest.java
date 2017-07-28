package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * CheckEmptyHousesAndSumResultsTest
 *
 * @author Stanislav Kyfenko
 */
public class CheckEmptyHousesAndSumResultsTest {

    private Game game;

    private FillBoard fillBoard = new FillBoard();

    private CheckEmptyHousesAndSumResults instanceToTest = new CheckEmptyHousesAndSumResults();

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        fillBoard.applyRule(game);
    }

    @Test
    public void applyRuleWithoutEmptyHouses() throws Exception {
        instanceToTest.applyRule(game);

        assertFalse("Game should NOT be over", game.isOver());
        assertNull("Winner should be NOT defined since game is not over", game.getWinner());
    }

    @Test
    public void applyRuleWithEmptyHousesAndSouthWinner() throws Exception {
        // set up 0 for south player
        int[] houses = game.getBoard().getHouses();
        for (int i = 0; i < Board.HOUSES_PER_PLAYER; i++) {
            houses[i] = 0;
        }
        houses[6] = 40;

        instanceToTest.applyRule(game);

        assertTrue("Game should be over", game.isOver());
        assertEquals("Winner should be defined since game is over", new SouthPlayer().name(), game.getWinner());
        Assert.assertArrayEquals("After game is finished, all the houses should be empty",
                new int[]{0, 0, 0, 0, 0, 0, 40, 0, 0, 0, 0, 0, 0, 36}, game.getBoard().getHouses());
    }

    @Test
    public void applyRuleWithEmptyHousesAndNorthWinner() throws Exception {
        // set up 0 for north player
        int[] houses = game.getBoard().getHouses();
        for (int i = 7; i < 14; i++) {
            houses[i] = 0;
        }
        houses[13] = 40;

        instanceToTest.applyRule(game);

        assertTrue("Game should be over", game.isOver());
        assertEquals("Winner should be defined since game is over", new NorthPlayer().name(), game.getWinner());
        Assert.assertArrayEquals("After game is finished, all the houses should be empty",
                new int[]{0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 40}, game.getBoard().getHouses());
    }


    @Test
    public void applyRuleWithEmptyHousesAndDraw() throws Exception {
        // set up 0 for south player
        int[] houses = game.getBoard().getHouses();
        for (int i = 0; i < Board.HOUSES_PER_PLAYER; i++) {
            houses[i] = 0;
        }
        houses[6] = 36; // setting 36 to south store (the same should be in the north store)

        instanceToTest.applyRule(game);

        assertTrue("Game should be over", game.isOver());
        assertEquals("DRAW should be set since game is over", "DRAW", game.getWinner());
        Assert.assertArrayEquals("After game is finished, all the houses should be empty",
                new int[]{0, 0, 0, 0, 0, 0, 36, 0, 0, 0, 0, 0, 0, 36}, game.getBoard().getHouses());
    }

}
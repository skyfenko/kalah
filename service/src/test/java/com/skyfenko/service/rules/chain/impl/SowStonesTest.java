package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import com.skyfenko.domain.turn.Turn;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * SowStonesTest
 *
 * @author Stanislav Kyfenko
 */
public class SowStonesTest {

    private Game game;
    private final FillBoard fillBoard = new FillBoard();
    private final SowStones sowStones = new SowStones();

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        fillBoard.applyRule(game);
    }

    @Test
    public void applyRuleForCorrectUser() throws Exception {
        game.setCurrentTurn(new Turn(3));
        game.setTakeTurnPlayer(new SouthPlayer());
        
        sowStones.applyRule(game);

        Assert.assertArrayEquals("After sowing, number of stones in the houses should be changed",
                new int[] {6, 6, 6, 0, 7, 7, 1, 7, 7, 7, 6, 6, 6, 0}, game.getBoard().getHouses());

        Assert.assertEquals("After sowing, last sown index for current turn should be kept",
                9, game.getCurrentTurn().getLastSownIndex());

    }

    @Test
    public void applyRuleForCorrectUserWith10StonesInTheHouse() throws Exception {
        game.setCurrentTurn(new Turn(3));
        game.setTakeTurnPlayer(new SouthPlayer());
        // changing value to 20 to check corner-case with opponent store. it should not be filled out in our attempt
        game.getBoard().getHouses()[3] = 20;


        sowStones.applyRule(game);

        Assert.assertArrayEquals("After sowing, number of stones in the houses should be changed",
                new int[] {7, 7, 7, 1, 8, 8, 2, 8, 8, 8, 8, 7, 7, 0}, game.getBoard().getHouses());

        Assert.assertEquals("After sowing, last sown index for current turn should be kept",
                10, game.getCurrentTurn().getLastSownIndex());

    }

    @Test
    public void applyRuleForIncorrectPlayer() throws Exception {
        game.setCurrentTurn(new Turn(3));
        game.setTakeTurnPlayer(new NorthPlayer());
        Board boardBeforeSowing = game.getBoard();
        sowStones.applyRule(game);
        Assert.assertArrayEquals("Sowing should NOT happen since player cannot take a turn", boardBeforeSowing.getHouses(), game.getBoard().getHouses());
    }
}
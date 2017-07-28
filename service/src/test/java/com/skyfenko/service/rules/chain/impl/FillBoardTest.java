package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * FillBoardTest
 *
 * @author Stanislav Kyfenko
 */
public class FillBoardTest {

    private Game game;

    private final FillBoard fillBoard = new FillBoard();

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new NorthPlayer(), new SouthPlayer()));
    }

    @Test
    public void applyRuleIfGameIsOver() throws Exception {
        game.setOver(Boolean.TRUE);
        fillBoard.applyRule(game);
        Assert.assertEquals("If game is over, elements in array should NOT be changed", 0, Arrays.stream(game.getBoard().getHouses()).sum());
    }

    @Test
    public void applyRuleIfGameIsNotOver() throws Exception {
        fillBoard.applyRule(game);

        int[] firstPlayerRangeIncludingStore = Arrays.copyOfRange(game.getBoard().getHouses(), 0, 7);
        int[] secondPlayerRangeIncludingStore = Arrays.copyOfRange(game.getBoard().getHouses(), 7, 14);

        Assert.assertArrayEquals("after initial filling, range for any player should be {6,6,6,6,6,6,0}", new int[]{6, 6, 6, 6, 6, 6, 0}, firstPlayerRangeIncludingStore);
        Assert.assertArrayEquals("after initial filling, range for any player should be {6,6,6,6,6,6,0}", new int[]{6, 6, 6, 6, 6, 6, 0}, secondPlayerRangeIncludingStore);
        Assert.assertTrue("Board should be filled", game.getBoard().isFilled());
        Assert.assertTrue("Game should be started", game.isStarted());
    }
}
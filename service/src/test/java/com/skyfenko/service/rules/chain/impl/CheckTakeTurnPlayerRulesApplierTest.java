package com.skyfenko.service.rules.chain.impl;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.game.Game;
import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * CheckTakeTurnPlayerRulesApplierTest
 *
 * @author Stanislav Kyfenko
 */
public class CheckTakeTurnPlayerRulesApplierTest {

    private CheckTakeTurnPlayerRulesApplier applier = new CheckTakeTurnPlayerRulesApplier() {
        @Override
        public void applyRule(Game game) {
            //nothing to do. we are just testing one method inside abstract class. no need to init implementor instance
        }

        @Override
        public int getOrder() {
            return 0;
        }
    };

    private Game game;

    @Before
    public void setUp() throws Exception {
        game = new Game(Arrays.asList(new SouthPlayer(), new NorthPlayer()));
        game.setTakeTurnPlayer(new SouthPlayer());
    }

    @Test
    public void checkPlayerCanTakeTurnWithStoreChosen() throws Exception {
        Assert.assertFalse(applier.canPlayerTakeTurn(game, game.getTakeTurnPlayer().storeIndex()));
    }

    @Test
    public void checkPlayerCanTakeTurnWithEdgeCellChosen() throws Exception {
        Assert.assertTrue(applier.canPlayerTakeTurn(game, game.getTakeTurnPlayer().storeIndex() - Board.HOUSES_PER_PLAYER));
    }

    @Test
    public void checkPlayerCanTakeTurn() throws Exception {
        Assert.assertTrue(applier.canPlayerTakeTurn(game, game.getTakeTurnPlayer().storeIndex() - 1));
        Assert.assertTrue(applier.canPlayerTakeTurn(game, game.getTakeTurnPlayer().storeIndex() - Board.HOUSES_PER_PLAYER + 1));
    }
}
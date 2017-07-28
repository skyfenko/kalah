package com.skyfenko.service.rules.orchestrator.impl;

import com.skyfenko.domain.player.impl.NorthPlayer;
import com.skyfenko.domain.player.impl.SouthPlayer;
import com.skyfenko.service.config.ServiceConfig;
import com.skyfenko.service.dto.GameRequest;
import com.skyfenko.service.dto.GameResponse;
import com.skyfenko.service.rules.chain.RulesApplier;
import com.skyfenko.service.rules.orchestrator.RulesOrchestrator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * RulesOrchestratorImplTest
 *
 * @author Stanislav Kyfenko
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServiceConfig.class})
public class RulesOrchestratorImplTest {

    @Autowired
    private RulesOrchestrator rulesOrchestrator;

    private static final String SOUTH = new SouthPlayer().name();
    private static final String NORTH = new NorthPlayer().name();

    @Test
    public void checkOrderOfAppliers() throws IllegalAccessException {
        // pull list of appliers by reflection in order not to expose redundant fields over interface contract
        Field appliers = ReflectionUtils.findField(RulesOrchestratorImpl.class, "rulesAppliers");
        ReflectionUtils.makeAccessible(appliers);
        List<RulesApplier> rulesAppliers = (List<RulesApplier>) appliers.get(rulesOrchestrator);

        for (int i = 0; i < rulesAppliers.size(); i++) {
            int order = rulesAppliers.get(i).getOrder();
            Assert.assertEquals("order of element in rules appliers chain should the same as order in list", i, order);
        }
    }

    @Before
    public void setUp() throws Exception {
        rulesOrchestrator.restartGame();
    }

    @Test
    public void testInit() throws Exception {
        GameResponse gameResponse = rulesOrchestrator.gameInfo();

        Assert.assertEquals("There should be 2 players to play game", 2, gameResponse.getPlayers().size());
        Assert.assertTrue("South player should be on the game", gameResponse.getPlayers().contains(SOUTH));
        Assert.assertTrue("North player should be on the game", gameResponse.getPlayers().contains(NORTH));
        Assert.assertEquals("South player should take a turn first", SOUTH, gameResponse.getTakeTurnPlayer());
        Assert.assertNull("No winner now", gameResponse.getWinner());
        Assert.assertArrayEquals("Board should be NOT initiated with non-zero values",
                new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                gameResponse.getBoard().getHouses());

    }

    @Test
    public void testChangeTakeTurnPlayer() {
        GameRequest gameRequest = new GameRequest(SOUTH);
        gameRequest.setCurrentTurn(1);

        GameResponse response = rulesOrchestrator.orchestrate(gameRequest);

        Assert.assertEquals("Take-turn-player should be changed to North", NORTH, response.getTakeTurnPlayer());
    }

    @Test
    public void testOrchestrate() throws Exception {
        GameRequest gameRequest = new GameRequest(SOUTH);
        gameRequest.setCurrentTurn(0);

        GameResponse response = rulesOrchestrator.orchestrate(gameRequest);

        Assert.assertArrayEquals("resulting board intermediate state is not matching to actual state",
                new int[]{0, 7, 7, 7, 7, 7, 1, 6, 6, 6, 6, 6, 6, 0}, response.getBoard().getHouses());

        Assert.assertEquals("South player should be given additional move", SOUTH, response.getTakeTurnPlayer());
        Assert.assertNull("No winner yet", response.getWinner());
    }

    @Test
    public void testRestartGame() throws Exception {
        testOrchestrate();
        GameResponse response = rulesOrchestrator.restartGame();
        Assert.assertEquals("There should be 2 players to play game", 2, response.getPlayers().size());
        Assert.assertTrue("South player should be on the game", response.getPlayers().contains(SOUTH));
        Assert.assertTrue("North player should be on the game", response.getPlayers().contains(NORTH));
        Assert.assertEquals("South player should take a turn first", SOUTH, response.getTakeTurnPlayer());
        Assert.assertNull("No winner now", response.getWinner());
        Assert.assertArrayEquals("Board should be NOT initiated with non-zero values",
                new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                response.getBoard().getHouses());
    }

}
package com.skyfenko.domain.player.impl;

import com.skyfenko.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * North Player. Assuming we count houses starting with 0 index and goes anti-clockwise.
 * North player, which is located at the top, should have own store at 13th index.
 *
 * @author Stanislav Kyfenko
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class NorthPlayer implements Player {

    private static final int STORE_INDEX_NORTH = 13;

    private static final String NORTH_PLAYER_NAME = "north";

    @Override
    public String name() {
        return NORTH_PLAYER_NAME;
    }

    @Override
    public int storeIndex() {
        return STORE_INDEX_NORTH;
    }
}

package com.skyfenko.domain.player.impl;

import com.skyfenko.domain.player.Player;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * South Player. Assuming we count houses starting with 0 index and goes anti-clockwise.
 * South player, which is located at the bottom, should have own store at 6th index.
 *
 * @author Stanislav Kyfenko
 */
@EqualsAndHashCode(callSuper = false)
@ToString
public class SouthPlayer implements Player {

    private static final int STORE_INDEX_SOUTH = 6;
    
    private static final String SOUTH_PLAYER_NAME = "south";

    @Override
    public String name() {
        return SOUTH_PLAYER_NAME;
    }

    @Override
    public int storeIndex() {
        return STORE_INDEX_SOUTH;
    }
}

package com.skyfenko.domain.board;

import com.skyfenko.domain.exceptions.impl.UnableToFetchStonesFromTheHouseException;
import lombok.Getter;
import lombok.Setter;

/**
 * Board. Contains up-to-date state for all the houses and stores. It's able to pull value of houses/stores by index.
 * Filled flag is set to true at the beginning of the game
 *
 * @author Stanislav Kyfenko
 */

public class Board {

    public static final int STONES_PER_HOUSE = 6;

    public static final int HOUSES_PER_PLAYER = 6;

    @Getter
    private final int[] houses = new int[14];

    @Setter
    @Getter
    private boolean filled = Boolean.FALSE;

    /**
     * Get number of stones in house or store by index
     *
     * @param index index to the house/store
     * @return number of stones inside
     */
    public int getByIndex(int index) {
        if (index >= houses.length || index < 0) {
            throw new UnableToFetchStonesFromTheHouseException("index " + index + " is invalid. Check it again.");
        }
        return houses[index];
    }

}

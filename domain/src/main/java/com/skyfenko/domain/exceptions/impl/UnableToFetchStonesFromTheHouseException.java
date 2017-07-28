package com.skyfenko.domain.exceptions.impl;

import com.skyfenko.domain.exceptions.KalahException;

/**
 * Thrown when we cannot pull house value from houses array in the {@link com.skyfenko.domain.board.Board}
 *
 * @author Stanislav Kyfenko
 */
public class UnableToFetchStonesFromTheHouseException extends KalahException {

    public UnableToFetchStonesFromTheHouseException(String message) {
        super(message);
    }
}

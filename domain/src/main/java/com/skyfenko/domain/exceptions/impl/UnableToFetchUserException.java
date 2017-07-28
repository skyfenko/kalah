package com.skyfenko.domain.exceptions.impl;

import com.skyfenko.domain.exceptions.KalahException;

/**
 * Thrown when we cannot find authenticated user in security context
 *
 * @author Stanislav Kyfenko
 */
public class UnableToFetchUserException extends KalahException {
    public UnableToFetchUserException(String message) {
        super(message);
    }
}

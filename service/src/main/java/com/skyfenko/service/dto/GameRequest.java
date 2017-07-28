package com.skyfenko.service.dto;

import lombok.*;

/**
 * Request from web to calculate results of current turn
 *
 * @author Stanislav Kyfenko
 */
@ToString
@Getter
@RequiredArgsConstructor
public class GameRequest {

    /**
     * Who takes turn?
     */
    @NonNull
    private String takeTurnPlayer;

    /**
     * chosen house from which move should be made
     */
    @Setter
    private int currentTurn;
}

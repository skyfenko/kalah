package com.skyfenko.service.dto;

import lombok.*;

import java.util.List;

/**
 * Representation for {@linkplain com.skyfenko.domain.game.Game} for web as response
 *
 * @author Stanislav Kyfenko
 */
@Builder(builderClassName = "Builder")
@Getter
@ToString
public class GameResponse {

    private BoardDTO board;

    private List<String> players;

    @lombok.Builder.Default
    private boolean over = Boolean.FALSE;

    @lombok.Builder.Default
    private boolean started = Boolean.FALSE;

    private String winner;

    private String takeTurnPlayer;
}

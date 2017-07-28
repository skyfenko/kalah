package com.skyfenko.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Representation of {@linkplain com.skyfenko.domain.board.Board} for web
 *
 * @author Stanislav Kyfenko
 */
@ToString
@Setter
@Getter
public class BoardDTO {

    private int[] houses;
}

package com.skyfenko.domain.turn;

import lombok.*;

/**
 * Turn.
 * chosen house - index in houses array to be the current turn.
 * last sown index - index of the cell in houses array where the last stone lands in the current turn
 *
 * @author Stanislav Kyfenko
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Turn {
    @NonNull
    private int chosenHouse;

    @Setter
    private int lastSownIndex;
}

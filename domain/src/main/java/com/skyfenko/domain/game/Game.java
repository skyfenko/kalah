package com.skyfenko.domain.game;

import com.skyfenko.domain.board.Board;
import com.skyfenko.domain.player.Player;
import com.skyfenko.domain.turn.Turn;
import lombok.*;

import java.util.List;

/**
 * Game. Contains :
 * {@link Board} with all the houses and stores
 * list of active players, who is winner?,
 * started flag (marked as true at the beginning of the game and keep as-is till the end of the game)
 * over flag (marked as true when the game is finished)
 * current turn (last sown index and chosen house to be the current turn)
 * take-turn-player
 *
 * @author Stanislav Kyfenko
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class Game {

    private final Board board = new Board();

    @NonNull
    private final List<Player> players;

    @Setter
    private boolean over = Boolean.FALSE;

    @Setter
    private boolean started = Boolean.FALSE;

    @Setter
    private String winner;

    @Setter
    private Turn currentTurn;

    @Setter
    private Turn previousTurn;

    @Setter
    private Player takeTurnPlayer;
}

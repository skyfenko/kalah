package com.skyfenko.web.constants;

/**
 * Hold all the URIs used by this app
 *
 * @author Stanislav Kyfenko
 */
public interface URIConstants {

    interface Api {
        String TURNS = "/api/kalah/turns";
        String GAME_INFO = "/api/kalah/game";
        String RESTART_GAME = "/api/kalah/game/restart";
    }

    interface Flow {
        String LOGIN = "/login";
        String LOGOUT = "/logout";
        String GAME = "/game";
        String ACCESS_DENIED = "/403";
    }
}

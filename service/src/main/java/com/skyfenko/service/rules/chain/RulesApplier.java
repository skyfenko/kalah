package com.skyfenko.service.rules.chain;

import com.skyfenko.domain.game.Game;
import org.springframework.core.Ordered;

/**
 * Main abstraction for all of rules applier.
 * <p>
 * This is some kind of Chain of Responsibility pattern in Spring ecosystem using {@link Ordered} interface.
 * The less order number {@link RulesApplier} instance has, the sooner it is called to process the current turn
 *
 * @author Stanislav Kyfenko
 * @see com.skyfenko.service.rules.chain.impl.FillBoard
 * @see com.skyfenko.service.rules.chain.impl.CheckTakeTurnPlayerRulesApplier
 * @see com.skyfenko.service.rules.chain.impl.SowStones
 * @see com.skyfenko.service.rules.chain.impl.LastSownInEmptyHouse
 * @see com.skyfenko.service.rules.chain.impl.LastSownInPlayerStore
 */
public interface RulesApplier extends Ordered {


    /**
     * main entry point for each element of the chain.
     * By appying the rule, it means that each chain element changes {@param game} based on rules, which it's responsible for
     *
     * @param game {@link Game}
     */
    void applyRule(Game game);

    /**
     * The less order number {@link RulesApplier} instance has, the sooner it is called to process the current turn
     *
     * @return order number
     * @see Ordered
     */
    int getOrder();
}

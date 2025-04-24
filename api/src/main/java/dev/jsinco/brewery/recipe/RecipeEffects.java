package dev.jsinco.brewery.recipe;

import dev.jsinco.brewery.effect.DrunksManager;

/**
 *
 * @param <P> Player object representative depending on the server API
 */
public interface RecipeEffects<P> {

    void applyTo(P player, DrunksManager drunksManager);
}

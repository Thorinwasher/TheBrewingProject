package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.recipe.RecipeEffects;
import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ApplyEffectEvent extends Cancellable {
    @Getter
    private final Wrapper<UUID, ?> player;
    @Getter
    @Setter
    private RecipeEffects<?> recipeEffects;

    protected ApplyEffectEvent(boolean cancelled, RecipeEffects<?> recipeEffects, Wrapper<UUID, ?> player) {
        super(cancelled);
        this.recipeEffects = recipeEffects;
        this.player = player;
    }
}

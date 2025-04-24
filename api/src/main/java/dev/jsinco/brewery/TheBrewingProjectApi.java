package dev.jsinco.brewery;

import dev.jsinco.brewery.brew.BrewManager;
import dev.jsinco.brewery.effect.DrunksManager;
import dev.jsinco.brewery.recipe.RecipeRegistry;
import dev.jsinco.brewery.structure.PlacedStructureRegistry;
import dev.jsinco.brewery.util.WrapperFactory;

public interface TheBrewingProjectApi {


    <I> BrewManager<I> getBrewManager();

    DrunksManager getDrunksManager();

    <I> RecipeRegistry<I> getRecipeRegistry();

    PlacedStructureRegistry getPlacedStructureRegistry();

    WrapperFactory getWrapperFactory();
}

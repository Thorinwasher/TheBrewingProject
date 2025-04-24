package dev.jsinco.brewery.breweries;

import dev.jsinco.brewery.util.Wrapper;
import dev.jsinco.brewery.vector.BreweryLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public interface InventoryAccessible<IS, I> {

    boolean open(@NotNull BreweryLocation breweryLocation, @NotNull UUID playerUuid);

    boolean inventoryAllows(@NotNull Wrapper<UUID, ?> player, @NotNull IS item);

    Set<I> getInventories();
}

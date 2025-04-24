package dev.jsinco.brewery.vector;

import dev.jsinco.brewery.util.Wrapper;

import java.util.UUID;

public record BreweryLocation(int x, int y, int z, Wrapper<UUID, ?> world) {

    public BreweryVector toVector() {
        return new BreweryVector(x, y, z);
    }

    public BreweryLocation add(int x, int y, int z) {
        return new BreweryLocation(x + x(), y + y(), z + z(), this.world);
    }
}

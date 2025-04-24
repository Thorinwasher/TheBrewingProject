package dev.jsinco.brewery.bukkit.util;

import dev.jsinco.brewery.util.BreweryKey;
import dev.jsinco.brewery.vector.BreweryLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BukkitAdapter {

    public static Optional<Location> toLocation(BreweryLocation location) {
        return WrapperFactoryImpl.worldWrapperType().value(location.world())
                .map(world -> new Location(world, location.x(), location.y(), location.z()));
    }

    public static BreweryLocation toBreweryLocation(Location location) {
        return new BreweryLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), WrapperFactoryImpl.worldWrapper(location.getWorld()));
    }

    public static BreweryLocation toBreweryLocation(Block block) {
        return new BreweryLocation(block.getX(), block.getY(), block.getZ(), WrapperFactoryImpl.worldWrapper(block.getWorld()));
    }

    public static Optional<Block> toBlock(BreweryLocation location) {
        return WrapperFactoryImpl.worldWrapperType().value(location.world())
                .map(world -> world.getBlockAt(location.x(), location.y(), location.z()));
    }

    /**
     * The format in brewery keys are different from namespacedkeys, can therefore return null
     */
    public static @Nullable NamespacedKey toNamespacedKey(BreweryKey breweryKey) {
        return NamespacedKey.fromString(breweryKey.toString());
    }

    public static @NotNull BreweryKey toBreweryKey(NamespacedKey namespacedKey) {
        return new BreweryKey(namespacedKey.namespace(), namespacedKey.getKey());
    }

    public static @Nullable Location parseLocation(String teleport) {
        String[] split = teleport.split(",");
        World world = Bukkit.getWorld(split[0]);
        if (world == null) {
            return null;
        }
        int x = Integer.parseInt(split[1].strip());
        int y = Integer.parseInt(split[2].strip());
        int z = Integer.parseInt(split[3].strip());
        return new Location(world, x, y, z);
    }
}

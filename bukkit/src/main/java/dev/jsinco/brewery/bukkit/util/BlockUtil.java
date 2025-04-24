package dev.jsinco.brewery.bukkit.util;

import dev.jsinco.brewery.vector.BreweryLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;

public class BlockUtil {

    public static boolean isChunkLoaded(BreweryLocation block) {
        return BukkitAdapter.toLocation(block)
                .map(Location::isChunkLoaded)
                .orElse(false);
    }

    public static boolean isLitCampfire(Block block) {
        if (block.getType() == Material.CAMPFIRE || block.getType() == Material.SOUL_CAMPFIRE) {
            return ((Lightable) block.getBlockData()).isLit();
        }
        return false;
    }

    public static boolean isSource(Block block) {
        if (block.getType() == Material.LAVA || block.getType() == Material.WATER) {
            return ((Levelled) block.getBlockData()).getLevel() == 0;
        }
        return false;
    }
}

package dev.jsinco.brewery.bukkit.util;

import com.google.common.base.Preconditions;
import dev.jsinco.brewery.util.Wrapper;
import dev.jsinco.brewery.util.WrapperFactory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WrapperFactoryImpl implements WrapperFactory {

    @Override
    public <W> Wrapper<UUID, W> createWorldWrapper(W worldUnchecked) {
        Preconditions.checkArgument(worldUnchecked instanceof World, "Expected a world object!");
        World world = (World) worldUnchecked;
        return (Wrapper<UUID, W>) new Wrapper<>(world.getUID(), WorldWrapperType.INSTANCE);
    }

    public static Wrapper<UUID, World> worldWrapper(@NotNull World world) {
        return new Wrapper<>(world.getUID(), WorldWrapperType.INSTANCE);
    }

    public static Wrapper.WrapperType<UUID, World> worldWrapperType() {
        return WorldWrapperType.INSTANCE;
    }

    @Override
    public <W> Wrapper.WorldWrapperType<W> getWorldWrapperType(Class<W> worldClass) {
        Preconditions.checkArgument(World.class == worldClass);
        return (Wrapper.WorldWrapperType<W>) WorldWrapperType.INSTANCE;
    }

    @Override
    public <P> Wrapper<UUID, P> createPlayerWrapper(P playerUnchecked) {
        Preconditions.checkArgument(playerUnchecked instanceof World, "Expected a world object!");
        Player player = (Player) playerUnchecked;
        return (Wrapper<UUID, P>) new Wrapper<>(player.getUniqueId(), PlayerWrapperType.INSTANCE);
    }

    @Override
    public <P> Wrapper.PlayerWrapperType<P> getPlayerWrapperType(Class<P> playerClass) {
        Preconditions.checkArgument(Player.class == playerClass);
        return (Wrapper.PlayerWrapperType<P>) PlayerWrapperType.INSTANCE;
    }

    public static Wrapper<UUID, Player> playerWrapper(@NotNull Player player) {
        return new Wrapper<>(player.getUniqueId(), PlayerWrapperType.INSTANCE);
    }

    public static Wrapper.WrapperType<UUID, Player> playerWrapperType() {
        return PlayerWrapperType.INSTANCE;
    }

    public static class WorldWrapperType implements Wrapper.WorldWrapperType<World> {

        public static final WorldWrapperType INSTANCE = new WorldWrapperType();

        @Override
        public @Nullable World transform(@NotNull UUID uuid) {
            return Bukkit.getWorld(uuid);
        }
    }

    private static class PlayerWrapperType implements Wrapper.PlayerWrapperType<Player> {

        public static final PlayerWrapperType INSTANCE = new PlayerWrapperType();

        @Override
        public @Nullable Player transform(@NotNull UUID uuid) {
            return Bukkit.getPlayer(uuid);
        }
    }
}

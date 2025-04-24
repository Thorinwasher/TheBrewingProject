package dev.jsinco.brewery.util;

import java.util.UUID;

public interface WrapperFactory {

    <W> Wrapper<UUID, W> createWorldWrapper(W world);

    <W> Wrapper.WorldWrapperType<W> getWorldWrapperType(Class<W> worldClass);

    <P> Wrapper<UUID, P> createPlayerWrapper(P player);

    <P> Wrapper.PlayerWrapperType<P> getPlayerWrapperType(Class<P> playerClass);
}

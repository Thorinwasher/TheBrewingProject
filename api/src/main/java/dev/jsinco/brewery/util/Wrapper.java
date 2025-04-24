package dev.jsinco.brewery.util;

import com.google.common.base.Preconditions;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class Wrapper<T, U> {

    @Getter
    private final T identifier;
    private final WrapperType<T, U> type;

    /**
     * Use {@link WrapperFactory#createWorldWrapper(Object)} instead
     *
     * @param identifier
     * @param type
     */
    @ApiStatus.Internal
    public Wrapper(@NotNull T identifier, @NotNull WrapperType<T, U> type) {
        Preconditions.checkNotNull(identifier);
        Preconditions.checkNotNull(type);
        this.identifier = identifier;
        this.type = type;
    }

    public Optional<U> get() {
        return type.value(this);
    }

    /**
     * @param identifier An identifier
     * @return A new wrapper with the specified identifier
     */
    public Wrapper<T, U> withIdentifier(T identifier) {
        return new Wrapper<>(identifier, this.type);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Wrapper<?, ?> wrapper)) {
            return false;
        }
        return identifier.equals(wrapper.identifier) && this.type == wrapper.type;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public String toString() {
        return "Wrapper(" + identifier.toString() + ")";
    }

    public interface WrapperType<T, U> {

        @Nullable U transform(@NotNull T t);

        default Optional<U> value(Wrapper<T, ?> wrapper) throws IllegalArgumentException {
            Preconditions.checkArgument(wrapper.type == this);
            return Optional.ofNullable(this.transform(wrapper.identifier));
        }

    }

    public interface PlayerWrapperType<P> extends WrapperType<UUID, P> {
    }

    public interface WorldWrapperType<W> extends WrapperType<UUID, W> {
    }
}

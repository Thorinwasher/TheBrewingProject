package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.event.DrunkEvent;
import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;

import java.util.UUID;

public class ApplyEventEvent extends Cancellable {
    @Getter
    private final DrunkEvent drunkEvent;
    @Getter
    private final Wrapper<UUID, ?> drunkard;

    protected ApplyEventEvent(boolean cancelled, DrunkEvent drunkEvent, Wrapper<UUID, ?> drunkard) {
        super(cancelled);
        this.drunkEvent = drunkEvent;
        this.drunkard = drunkard;
    }

}

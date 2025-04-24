package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.event.DrunkEvent;
import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ScheduleEventEvent extends Cancellable implements Event {
    @Getter
    private final Wrapper<UUID, ?> drunkard;
    @Setter
    @Getter
    private long timestamp;
    @Setter
    @Getter
    private DrunkEvent event;

    protected ScheduleEventEvent(boolean cancelled, long timestamp, DrunkEvent event, Wrapper<UUID, ?> drunkard) {
        super(cancelled);
        this.timestamp = timestamp;
        this.event = event;
        this.drunkard = drunkard;
    }
}

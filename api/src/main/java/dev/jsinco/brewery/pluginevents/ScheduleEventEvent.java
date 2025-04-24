package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.event.DrunkEvent;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ScheduleEventEvent extends Cancellable implements Event {
    @Getter
    private final UUID drunkard;
    @Setter
    @Getter
    private long timestamp;
    @Setter
    @Getter
    private DrunkEvent event;

    protected ScheduleEventEvent(boolean cancelled, long timestamp, DrunkEvent event, UUID drunkard) {
        super(cancelled);
        this.timestamp = timestamp;
        this.event = event;
        this.drunkard = drunkard;
    }
}

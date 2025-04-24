package dev.jsinco.brewery.pluginevents;

public class ScheduleEventEvent extends Cancellable implements Event {
    protected ScheduleEventEvent(boolean cancelled) {
        super(cancelled);
    }
}

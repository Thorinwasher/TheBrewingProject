package dev.jsinco.brewery.pluginevents;

public class ApplyEffectEvent extends Cancellable{
    protected ApplyEffectEvent(boolean cancelled) {
        super(cancelled);
    }
}

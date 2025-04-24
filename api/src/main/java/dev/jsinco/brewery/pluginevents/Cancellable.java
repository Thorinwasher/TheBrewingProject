package dev.jsinco.brewery.pluginevents;

public class Cancellable {

    private boolean cancelled;

    protected Cancellable(boolean cancelled){
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}

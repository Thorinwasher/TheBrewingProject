package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.structure.Structure;
import lombok.Getter;

public class StructureDestroyEvent extends Cancellable {
    @Getter
    private final Structure structure;

    protected StructureDestroyEvent(boolean cancelled, Structure structure) {
        super(cancelled);
        this.structure = structure;
    }
}

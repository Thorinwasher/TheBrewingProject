package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.structure.MultiBlockStructure;

public class StructureDestroyEvent extends Cancellable {
    private final MultiBlockStructure<?> structure;

    protected StructureDestroyEvent(boolean cancelled, MultiBlockStructure<?> structure) {
        super(cancelled);
        this.structure = structure;
    }

    public MultiBlockStructure<?> getStructure() {
        return this.structure;
    }
}

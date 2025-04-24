package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.structure.MultiBlockStructure;

public class CreateStructureEvent extends Permissible implements Event {
    private MultiBlockStructure<?> structure;

    public CreateStructureEvent(String permissionNode, boolean permission, boolean cancelled, String denyMessage, MultiBlockStructure<?> structure) {
        super(permissionNode, permission, cancelled, denyMessage);
        this.structure = structure;
    }

    public void setStructure(MultiBlockStructure<?> structure) {
        this.structure = structure;
    }

    public MultiBlockStructure<?> getStructure() {
        return structure;
    }
}

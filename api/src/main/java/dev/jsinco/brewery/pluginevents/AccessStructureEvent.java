package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.structure.Structure;
import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;

import java.util.UUID;

public class AccessStructureEvent extends Permissible implements Event {
    @Getter
    private final Structure structure;

    protected AccessStructureEvent(String permissionNode, boolean permission, boolean cancelled, String denyMessage, Structure structure, Wrapper<UUID, ?> player) {
        super(permissionNode, permission, cancelled, denyMessage, player);
        this.structure = structure;
    }
}

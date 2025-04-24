package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.structure.Structure;
import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class CreateStructureEvent extends Permissible implements Event {
    @Setter
    @Getter
    private Structure structure;

    public CreateStructureEvent(String permissionNode, boolean permission, boolean cancelled, String denyMessage, Wrapper<UUID, ?> player, Structure structure) {
        super(permissionNode, permission, cancelled, denyMessage, player);
        this.structure = structure;
    }
}

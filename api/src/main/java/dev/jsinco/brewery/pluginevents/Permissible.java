package dev.jsinco.brewery.pluginevents;

import dev.jsinco.brewery.util.Wrapper;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Permissible extends Cancellable {

    @Getter
    private final String permissionNode;
    @Setter
    @Getter
    private Wrapper<UUID, ?> player;
    @Setter
    private String denyMessage;
    @Setter
    @Getter
    private boolean permission;

    protected Permissible(String permissionNode, boolean permission, boolean cancelled, String denyMessage, Wrapper<UUID, ?> player) {
        super(cancelled);
        this.permissionNode = permissionNode;
        this.permission = permission;
        this.denyMessage = denyMessage;
        this.player = player;
    }

}

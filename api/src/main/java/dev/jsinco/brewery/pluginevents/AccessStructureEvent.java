package dev.jsinco.brewery.pluginevents;

public class AccessStructureEvent extends Permissible {
    protected AccessStructureEvent(String permissionNode, boolean permission, boolean cancelled, String denyMessage) {
        super(permissionNode, permission, cancelled, denyMessage);
    }
}

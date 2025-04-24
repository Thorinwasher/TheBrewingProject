package dev.jsinco.brewery.pluginevents;

public class Permissible extends Cancellable{

    private final String permissionsNode;
    private String denyMessage;
    private boolean permission;

    protected Permissible(String permissionNode, boolean permission, boolean cancelled, String denyMessage) {
        super(cancelled);
        this.permissionsNode = permissionNode;
        this.permission = permission;
        this.denyMessage = denyMessage;
    }

    public String getPermissionNode() {
        return this.permissionsNode;
    }

    public boolean hasPermission() {
        return this.permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }
}

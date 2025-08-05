package org.causeway.poc.security;

/**
 * Enumeration representing permission modes.
 * Simplified version from secman ApplicationPermissionMode.
 */
public enum PermissionMode {
    VIEWING("Viewing"),
    CHANGING("Changing");
    
    private final String displayName;
    
    PermissionMode(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
package org.causeway.poc.security;

/**
 * Enumeration representing permission rules.
 * Simplified version from secman ApplicationPermissionRule.
 */
public enum PermissionRule {
    ALLOW("Allow"),
    VETO("Veto");
    
    private final String displayName;
    
    PermissionRule(String displayName) {
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
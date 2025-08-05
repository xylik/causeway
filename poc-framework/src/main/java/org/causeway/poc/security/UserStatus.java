package org.causeway.poc.security;

/**
 * Enumeration representing the status of a user account.
 * Simplified version from secman ApplicationUserStatus.
 */
public enum UserStatus {
    UNLOCKED("Unlocked"),
    LOCKED("Locked"),
    DISABLED("Disabled");
    
    private final String displayName;
    
    UserStatus(String displayName) {
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
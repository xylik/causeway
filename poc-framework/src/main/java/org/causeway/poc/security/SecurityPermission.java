package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;

/**
 * Represents a permission in the security system.
 * Links a role to a specific application feature with rules and modes.
 * Simplified version of ApplicationPermission from secman.
 */
@DomainObject
public class SecurityPermission {
    
    private SecurityRole role;
    private String featureId;
    private PermissionRule rule;
    private PermissionMode mode;
    
    // Constructors
    public SecurityPermission() {}
    
    public SecurityPermission(SecurityRole role, String featureId, PermissionRule rule, PermissionMode mode) {
        this.role = role;
        this.featureId = featureId;
        this.rule = rule;
        this.mode = mode;
    }
    
    // Getters and Setters
    @Property
    public SecurityRole getRole() { return role; }
    public void setRole(SecurityRole role) { this.role = role; }
    
    @Property
    public String getFeatureId() { return featureId; }
    public void setFeatureId(String featureId) { this.featureId = featureId; }
    
    @Property
    public PermissionRule getRule() { return rule; }
    public void setRule(PermissionRule rule) { this.rule = rule; }
    
    @Property
    public PermissionMode getMode() { return mode; }
    public void setMode(PermissionMode mode) { this.mode = mode; }
    
    // Business Methods
    public boolean grants(String targetFeatureId, PermissionMode targetMode) {
        if (!featureId.equals(targetFeatureId)) {
            return false;
        }
        
        if (rule == PermissionRule.VETO) {
            return false;
        }
        
        // For ALLOW rules, check if this permission's mode covers the target mode
        // CHANGING permission implies VIEWING permission
        return mode == targetMode || 
               (mode == PermissionMode.CHANGING && targetMode == PermissionMode.VIEWING);
    }
    
    public boolean vetoes(String targetFeatureId, PermissionMode targetMode) {
        if (!featureId.equals(targetFeatureId)) {
            return false;
        }
        
        if (rule == PermissionRule.ALLOW) {
            return false;
        }
        
        // For VETO rules, check if this permission's mode covers the target mode
        // VIEWING veto implies CHANGING veto
        return mode == targetMode || 
               (mode == PermissionMode.VIEWING && targetMode == PermissionMode.CHANGING);
    }
    
    @Override
    public String toString() {
        return String.format("SecurityPermission[%s:%s %s %s]", 
            role != null ? role.getName() : "null", featureId, rule, mode);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityPermission other = (SecurityPermission) obj;
        return role != null && role.equals(other.role) &&
               featureId != null && featureId.equals(other.featureId) &&
               rule == other.rule && mode == other.mode;
    }
    
    @Override
    public int hashCode() {
        int result = role != null ? role.hashCode() : 0;
        result = 31 * result + (featureId != null ? featureId.hashCode() : 0);
        result = 31 * result + (rule != null ? rule.hashCode() : 0);
        result = 31 * result + (mode != null ? mode.hashCode() : 0);
        return result;
    }
}
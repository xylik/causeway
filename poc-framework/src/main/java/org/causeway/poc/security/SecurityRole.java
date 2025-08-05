package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;
import org.causeway.poc.annotation.Collection;
import org.causeway.poc.annotation.Action;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a role in the security system.
 * Simplified version of ApplicationRole from secman.
 */
@DomainObject
public class SecurityRole {
    
    private String name;
    private String description;
    private Set<SecurityUser> users = new HashSet<>();
    private Set<SecurityPermission> permissions = new HashSet<>();
    
    // Constructors
    public SecurityRole() {}
    
    public SecurityRole(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    @Property
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Property
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Collection
    public Set<SecurityUser> getUsers() { return users; }
    public void setUsers(Set<SecurityUser> users) { this.users = users; }
    
    @Collection
    public Set<SecurityPermission> getPermissions() { return permissions; }
    public void setPermissions(Set<SecurityPermission> permissions) { this.permissions = permissions; }
    
    // Business Methods
    @Action
    public void addUser(SecurityUser user) {
        this.users.add(user);
        user.getRoles().add(this);
    }
    
    @Action
    public void removeUser(SecurityUser user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }
    
    @Action
    public void addPermission(SecurityPermission permission) {
        this.permissions.add(permission);
        permission.setRole(this);
    }
    
    @Action
    public void removePermission(SecurityPermission permission) {
        this.permissions.remove(permission);
        permission.setRole(null);
    }
    
    public boolean hasPermission(String featureId, PermissionMode mode) {
        return permissions.stream()
            .anyMatch(p -> p.getFeatureId().equals(featureId) && 
                          p.getMode().ordinal() >= mode.ordinal() &&
                          p.getRule() == PermissionRule.ALLOW);
    }
    
    @Override
    public String toString() {
        return String.format("SecurityRole[%s]", name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityRole other = (SecurityRole) obj;
        return name != null && name.equals(other.name);
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
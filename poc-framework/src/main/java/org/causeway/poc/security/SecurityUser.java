package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;
import org.causeway.poc.annotation.Collection;
import org.causeway.poc.annotation.Action;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a user in the security system.
 * Simplified version of ApplicationUser from secman.
 */
@DomainObject
public class SecurityUser {
    
    private String username;
    private String name;
    private String email;
    private UserStatus status;
    private Set<SecurityRole> roles = new HashSet<>();
    
    // Constructors
    public SecurityUser() {}
    
    public SecurityUser(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.status = UserStatus.UNLOCKED;
    }
    
    // Getters and Setters
    @Property
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    @Property
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    @Property
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @Property
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    @Collection
    public Set<SecurityRole> getRoles() { return roles; }
    public void setRoles(Set<SecurityRole> roles) { this.roles = roles; }
    
    // Business Methods
    @Action
    public void lock() {
        this.status = UserStatus.LOCKED;
    }
    
    @Action
    public void unlock() {
        this.status = UserStatus.UNLOCKED;
    }
    
    @Action
    public void addRole(SecurityRole role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
    
    @Action
    public void removeRole(SecurityRole role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    public boolean isLocked() {
        return status == UserStatus.LOCKED;
    }
    
    public boolean hasPermission(String featureId, PermissionMode mode) {
        return roles.stream().anyMatch(role -> role.hasPermission(featureId, mode));
    }
    
    @Override
    public String toString() {
        return String.format("SecurityUser[%s (%s)]", username, name);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecurityUser other = (SecurityUser) obj;
        return username != null && username.equals(other.username);
    }
    
    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
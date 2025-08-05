package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Security service that manages authentication and authorization.
 * Simplified version inspired by secman's security integration.
 */
@Service
@DomainService
public class SecurityService {
    
    private final SecurityUserRepository userRepository;
    private SecurityUser currentUser;
    
    public SecurityService(SecurityUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // Authentication
    public boolean authenticate(String username, String password) {
        Optional<SecurityUser> user = userRepository.findByUsername(username);
        if (user.isPresent() && !user.get().isLocked()) {
            // In a real implementation, this would check the password hash
            // For demo purposes, we'll accept any non-empty password
            if (password != null && !password.trim().isEmpty()) {
                this.currentUser = user.get();
                return true;
            }
        }
        return false;
    }
    
    public void logout() {
        this.currentUser = null;
    }
    
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    public SecurityUser getCurrentUser() {
        return currentUser;
    }
    
    public Optional<String> getCurrentUsername() {
        return currentUser != null ? Optional.of(currentUser.getUsername()) : Optional.empty();
    }
    
    // Authorization
    public boolean hasRole(String roleName) {
        return currentUser != null && currentUser.hasRole(roleName);
    }
    
    public boolean hasAnyRole(String... roleNames) {
        if (currentUser == null) return false;
        for (String roleName : roleNames) {
            if (currentUser.hasRole(roleName)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasAllRoles(String... roleNames) {
        if (currentUser == null) return false;
        for (String roleName : roleNames) {
            if (!currentUser.hasRole(roleName)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasPermission(String featureId, PermissionMode mode) {
        return currentUser != null && currentUser.hasPermission(featureId, mode);
    }
    
    public boolean canView(String featureId) {
        return hasPermission(featureId, PermissionMode.VIEWING);
    }
    
    public boolean canChange(String featureId) {
        return hasPermission(featureId, PermissionMode.CHANGING);
    }
    
    // Security checks
    public void requireAuthentication() {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required");
        }
    }
    
    public void requireRole(String roleName) {
        requireAuthentication();
        if (!hasRole(roleName)) {
            throw new SecurityException("Access denied. Required role: " + roleName);
        }
    }
    
    public void requireAnyRole(String... roleNames) {
        requireAuthentication();
        if (!hasAnyRole(roleNames)) {
            throw new SecurityException("Access denied. Required any of roles: " + String.join(", ", roleNames));
        }
    }
    
    public void requireAllRoles(String... roleNames) {
        requireAuthentication();
        if (!hasAllRoles(roleNames)) {
            throw new SecurityException("Access denied. Required all roles: " + String.join(", ", roleNames));
        }
    }
    
    public void requirePermission(String featureId, PermissionMode mode) {
        requireAuthentication();
        if (!hasPermission(featureId, mode)) {
            throw new SecurityException("Access denied. Required permission: " + featureId + " (" + mode + ")");
        }
    }
    
    // Utility method to generate feature IDs
    public String generateFeatureId(Class<?> clazz, String memberName) {
        return clazz.getSimpleName() + "." + memberName;
    }
    
    public String generateFeatureId(Object instance, String memberName) {
        return generateFeatureId(instance.getClass(), memberName);
    }
}
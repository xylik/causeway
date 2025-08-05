package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainService;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing SecurityPermission entities.
 * Simplified version inspired by secman's ApplicationPermissionRepository.
 */
@Repository
@DomainService
public class SecurityPermissionRepository {
    
    private final Map<String, SecurityPermission> permissions = new ConcurrentHashMap<>();
    
    // Basic CRUD operations
    public SecurityPermission save(SecurityPermission permission) {
        String key = generateKey(permission);
        permissions.put(key, permission);
        return permission;
    }
    
    public Optional<SecurityPermission> findByRoleAndFeature(SecurityRole role, String featureId) {
        return permissions.values().stream()
            .filter(p -> p.getRole().equals(role) && p.getFeatureId().equals(featureId))
            .findFirst();
    }
    
    public List<SecurityPermission> findAll() {
        return new ArrayList<>(permissions.values());
    }
    
    public List<SecurityPermission> findByRole(SecurityRole role) {
        return permissions.values().stream()
            .filter(p -> p.getRole().equals(role))
            .collect(Collectors.toList());
    }
    
    public List<SecurityPermission> findByFeature(String featureId) {
        return permissions.values().stream()
            .filter(p -> p.getFeatureId().equals(featureId))
            .collect(Collectors.toList());
    }
    
    public List<SecurityPermission> findByRule(PermissionRule rule) {
        return permissions.values().stream()
            .filter(p -> p.getRule() == rule)
            .collect(Collectors.toList());
    }
    
    public List<SecurityPermission> findByMode(PermissionMode mode) {
        return permissions.values().stream()
            .filter(p -> p.getMode() == mode)
            .collect(Collectors.toList());
    }
    
    public void delete(SecurityPermission permission) {
        String key = generateKey(permission);
        permissions.remove(key);
    }
    
    public void delete(SecurityRole role, String featureId) {
        permissions.values().removeIf(p -> p.getRole().equals(role) && p.getFeatureId().equals(featureId));
    }
    
    public long count() {
        return permissions.size();
    }
    
    public void clear() {
        permissions.clear();
    }
    
    // Business methods
    public SecurityPermission createPermission(SecurityRole role, String featureId, 
                                             PermissionRule rule, PermissionMode mode) {
        SecurityPermission permission = new SecurityPermission(role, featureId, rule, mode);
        return save(permission);
    }
    
    public SecurityPermission grantPermission(SecurityRole role, String featureId, PermissionMode mode) {
        return createPermission(role, featureId, PermissionRule.ALLOW, mode);
    }
    
    public SecurityPermission vetoPermission(SecurityRole role, String featureId, PermissionMode mode) {
        return createPermission(role, featureId, PermissionRule.VETO, mode);
    }
    
    public void revokePermission(SecurityRole role, String featureId) {
        delete(role, featureId);
    }
    
    // Helper methods
    private String generateKey(SecurityPermission permission) {
        return permission.getRole().getName() + ":" + permission.getFeatureId() + 
               ":" + permission.getRule() + ":" + permission.getMode();
    }
}
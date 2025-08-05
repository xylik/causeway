package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainService;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing SecurityRole entities.
 * Simplified version inspired by secman's ApplicationRoleRepository.
 */
@Repository
@DomainService
public class SecurityRoleRepository {
    
    private final Map<String, SecurityRole> roles = new ConcurrentHashMap<>();
    
    // Basic CRUD operations
    public SecurityRole save(SecurityRole role) {
        roles.put(role.getName(), role);
        return role;
    }
    
    public Optional<SecurityRole> findByName(String name) {
        return Optional.ofNullable(roles.get(name));
    }
    
    public List<SecurityRole> findAll() {
        return new ArrayList<>(roles.values());
    }
    
    public List<SecurityRole> findMatching(String searchText) {
        String search = searchText.toLowerCase();
        return roles.values().stream()
            .filter(role -> 
                role.getName().toLowerCase().contains(search) ||
                role.getDescription().toLowerCase().contains(search))
            .collect(Collectors.toList());
    }
    
    public List<SecurityRole> findByUser(SecurityUser user) {
        return roles.values().stream()
            .filter(role -> role.getUsers().contains(user))
            .collect(Collectors.toList());
    }
    
    public void delete(SecurityRole role) {
        roles.remove(role.getName());
    }
    
    public void delete(String name) {
        roles.remove(name);
    }
    
    public boolean exists(String name) {
        return roles.containsKey(name);
    }
    
    public long count() {
        return roles.size();
    }
    
    public void clear() {
        roles.clear();
    }
    
    // Business methods
    public SecurityRole createRole(String name, String description) {
        if (exists(name)) {
            throw new IllegalArgumentException("Role already exists: " + name);
        }
        SecurityRole role = new SecurityRole(name, description);
        return save(role);
    }
    
    public SecurityRole getOrCreateRole(String name, String description) {
        return findByName(name).orElseGet(() -> createRole(name, description));
    }
}
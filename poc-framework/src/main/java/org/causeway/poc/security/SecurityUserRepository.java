package org.causeway.poc.security;

import org.causeway.poc.annotation.DomainService;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for managing SecurityUser entities.
 * Simplified version inspired by secman's ApplicationUserRepository.
 */
@Repository
@DomainService
public class SecurityUserRepository {
    
    private final Map<String, SecurityUser> users = new ConcurrentHashMap<>();
    
    // Basic CRUD operations
    public SecurityUser save(SecurityUser user) {
        users.put(user.getUsername(), user);
        return user;
    }
    
    public Optional<SecurityUser> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }
    
    public Optional<SecurityUser> findByEmail(String email) {
        return users.values().stream()
            .filter(user -> email.equals(user.getEmail()))
            .findFirst();
    }
    
    public List<SecurityUser> findAll() {
        return new ArrayList<>(users.values());
    }
    
    public List<SecurityUser> findByStatus(UserStatus status) {
        return users.values().stream()
            .filter(user -> user.getStatus() == status)
            .collect(Collectors.toList());
    }
    
    public List<SecurityUser> findByRole(SecurityRole role) {
        return users.values().stream()
            .filter(user -> user.getRoles().contains(role))
            .collect(Collectors.toList());
    }
    
    public List<SecurityUser> findMatching(String searchText) {
        String search = searchText.toLowerCase();
        return users.values().stream()
            .filter(user -> 
                user.getUsername().toLowerCase().contains(search) ||
                user.getName().toLowerCase().contains(search) ||
                user.getEmail().toLowerCase().contains(search))
            .collect(Collectors.toList());
    }
    
    public void delete(SecurityUser user) {
        users.remove(user.getUsername());
    }
    
    public void delete(String username) {
        users.remove(username);
    }
    
    public boolean exists(String username) {
        return users.containsKey(username);
    }
    
    public long count() {
        return users.size();
    }
    
    public void clear() {
        users.clear();
    }
    
    // Business methods
    public SecurityUser createUser(String username, String name, String email) {
        if (exists(username)) {
            throw new IllegalArgumentException("User already exists: " + username);
        }
        SecurityUser user = new SecurityUser(username, name, email);
        return save(user);
    }
    
    public SecurityUser lockUser(String username) {
        SecurityUser user = findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.lock();
        return save(user);
    }
    
    public SecurityUser unlockUser(String username) {
        SecurityUser user = findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        user.unlock();
        return save(user);
    }
}
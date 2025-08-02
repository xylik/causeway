package org.causeway.poc.objectmanager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.causeway.poc.events.EventBusService;
import org.causeway.poc.metamodel.ActionSpec;
import org.causeway.poc.metamodel.MetamodelService;
import org.causeway.poc.metamodel.ObjectSpec;

/**
 * Service for managing domain object lifecycle and interactions.
 * Inspired by Apache Causeway's ObjectManager.
 */
@Service
public class ObjectManager {
    
    private final MetamodelService metamodelService;
    private final EventBusService eventBusService;
    private final Map<String, Object> objectRepository = new ConcurrentHashMap<>();
    
    public ObjectManager(MetamodelService metamodelService, EventBusService eventBusService) {
        this.metamodelService = metamodelService;
        this.eventBusService = eventBusService;
    }
    
    /**
     * Store an object in the repository with a generated ID.
     */
    public String persist(Object object) {
        if (!metamodelService.isDomainObject(object.getClass())) {
            throw new IllegalArgumentException("Object is not a domain object: " + object.getClass());
        }
        
        String id = generateId(object);
        objectRepository.put(id, object);
        return id;
    }
    
    /**
     * Retrieve an object by ID.
     */
    public Optional<Object> findById(String id) {
        return Optional.ofNullable(objectRepository.get(id));
    }
    
    /**
     * Get all objects of a specific type.
     */
    public java.util.Collection<Object> findAll(Class<?> type) {
        return objectRepository.values().stream()
            .filter(obj -> type.isAssignableFrom(obj.getClass()))
            .toList();
    }
    
    /**
     * Invoke an action on an object, publishing events.
     */
    public Object invokeAction(Object target, String actionId, Object... args) {
        ObjectSpec spec = metamodelService.loadSpecification(target);
        
        ActionSpec actionSpec = spec.getActions().stream()
            .filter(a -> a.getId().equals(actionId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Action not found: " + actionId));
        
        return eventBusService.publishActionInvocation(target, actionId, args, 
            () -> actionSpec.invoke(target, args));
    }
    
    /**
     * Get the specification for an object.
     */
    public ObjectSpec getSpecification(Object object) {
        return metamodelService.loadSpecification(object);
    }
    
    /**
     * Get all managed objects.
     */
    public java.util.Collection<Object> getAllObjects() {
        return objectRepository.values();
    }
    
    private String generateId(Object object) {
        return object.getClass().getSimpleName() + "_" + object.hashCode();
    }
}
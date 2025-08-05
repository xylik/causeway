package org.causeway.poc.metamodel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import org.causeway.poc.annotation.DomainObject;

/**
 * Service that manages the metamodel - the specifications of domain objects.
 * Inspired by Apache Causeway's SpecificationLoader.
 */
@Service
public class MetamodelService {
    
    private final Map<Class<?>, ObjectSpec> specByClass = new ConcurrentHashMap<>();
    
    /**
     * Load or retrieve the specification for a given class.
     */
    public ObjectSpec loadSpecification(Class<?> clazz) {
        return specByClass.computeIfAbsent(clazz, this::createSpecification);
    }
    
    /**
     * Load or retrieve the specification for a given object.
     */
    public ObjectSpec loadSpecification(Object object) {
        return loadSpecification(object.getClass());
    }
    
    private ObjectSpec createSpecification(Class<?> clazz) {
        // Only create specs for domain objects
        if (clazz.isAnnotationPresent(DomainObject.class)) {
            return new ObjectSpec(clazz);
        }
        throw new IllegalArgumentException("Class " + clazz + " is not a domain object");
    }
    
    /**
     * Check if a class is a domain object.
     */
    public boolean isDomainObject(Class<?> clazz) {
        return clazz.isAnnotationPresent(DomainObject.class);
    }
    
    /**
     * Get all loaded specifications.
     */
    public java.util.Collection<ObjectSpec> getAllSpecs() {
        return specByClass.values();
    }
}
package org.causeway.poc.metamodel;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.causeway.poc.annotation.Action;
import org.causeway.poc.annotation.Collection;
import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;

/**
 * Metamodel representation of a domain object specification.
 * Inspired by Apache Causeway's ObjectSpecification.
 */
public class ObjectSpec {
    
    private final Class<?> correspondingClass;
    private final String objectType;
    private final DomainObject.Nature nature;
    private final List<PropertySpec> properties = new ArrayList<>();
    private final List<ActionSpec> actions = new ArrayList<>();
    private final List<CollectionSpec> collections = new ArrayList<>();
    
    public ObjectSpec(Class<?> correspondingClass) {
        this.correspondingClass = correspondingClass;
        
        DomainObject domainObject = correspondingClass.getAnnotation(DomainObject.class);
        if (domainObject != null) {
            this.objectType = domainObject.objectType().isEmpty() ? 
                correspondingClass.getSimpleName() : domainObject.objectType();
            this.nature = domainObject.nature();
        } else {
            this.objectType = correspondingClass.getSimpleName();
            this.nature = DomainObject.Nature.ENTITY;
        }
        
        introspectMembers();
    }
    
    private void introspectMembers() {
        for (Method method : correspondingClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Property.class)) {
                properties.add(new PropertySpec(method));
            } else if (method.isAnnotationPresent(Action.class)) {
                actions.add(new ActionSpec(method));
            } else if (method.isAnnotationPresent(Collection.class)) {
                collections.add(new CollectionSpec(method));
            }
        }
    }
    
    // Getters
    public Class<?> getCorrespondingClass() { return correspondingClass; }
    public String getObjectType() { return objectType; }
    public DomainObject.Nature getNature() { return nature; }
    public List<PropertySpec> getProperties() { return properties; }
    public List<ActionSpec> getActions() { return actions; }
    public List<CollectionSpec> getCollections() { return collections; }
    
    public String getTitle() {
        return objectType;
    }
    
    @Override
    public String toString() {
        return "ObjectSpec{" +
                "objectType='" + objectType + '\'' +
                ", nature=" + nature +
                ", properties=" + properties.size() +
                ", actions=" + actions.size() +
                ", collections=" + collections.size() +
                '}';
    }
}
package org.causeway.poc.metamodel;

import java.lang.reflect.Method;

import org.causeway.poc.annotation.Collection;

/**
 * Metamodel representation of a collection.
 */
public class CollectionSpec {
    
    private final Method method;
    private final String id;
    private final String name;
    private final Collection.Editing editing;
    
    public CollectionSpec(Method method) {
        this.method = method;
        this.id = method.getName();
        this.name = deriveNameFromMethod(method);
        
        Collection collection = method.getAnnotation(Collection.class);
        if (collection != null) {
            this.editing = collection.editing();
        } else {
            this.editing = Collection.Editing.ENABLED;
        }
    }
    
    private String deriveNameFromMethod(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return methodName.substring(3);
        }
        return methodName;
    }
    
    public Object getValue(Object target) {
        try {
            method.setAccessible(true);
            return method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get collection value", e);
        }
    }
    
    // Getters
    public Method getMethod() { return method; }
    public String getId() { return id; }
    public String getName() { return name; }
    public Collection.Editing getEditing() { return editing; }
    public Class<?> getType() { return method.getReturnType(); }
    
    @Override
    public String toString() {
        return "CollectionSpec{" +
                "name='" + name + '\'' +
                ", type=" + getType().getSimpleName() +
                ", editing=" + editing +
                '}';
    }
}
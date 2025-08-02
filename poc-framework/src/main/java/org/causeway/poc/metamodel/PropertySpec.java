package org.causeway.poc.metamodel;

import java.lang.reflect.Method;

import org.causeway.poc.annotation.Property;

/**
 * Metamodel representation of a property.
 */
public class PropertySpec {
    
    private final Method method;
    private final String id;
    private final String name;
    private final Property.Editing editing;
    private final boolean mandatory;
    private final int maxLength;
    
    public PropertySpec(Method method) {
        this.method = method;
        this.id = method.getName();
        this.name = deriveNameFromMethod(method);
        
        Property property = method.getAnnotation(Property.class);
        if (property != null) {
            this.editing = property.editing();
            this.mandatory = property.mandatory();
            this.maxLength = property.maxLength();
        } else {
            this.editing = Property.Editing.ENABLED;
            this.mandatory = false;
            this.maxLength = -1;
        }
    }
    
    private String deriveNameFromMethod(Method method) {
        String methodName = method.getName();
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return methodName.substring(3);
        } else if (methodName.startsWith("is") && methodName.length() > 2) {
            return methodName.substring(2);
        }
        return methodName;
    }
    
    public Object getValue(Object target) {
        try {
            method.setAccessible(true);
            return method.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get property value", e);
        }
    }
    
    // Getters
    public Method getMethod() { return method; }
    public String getId() { return id; }
    public String getName() { return name; }
    public Property.Editing getEditing() { return editing; }
    public boolean isMandatory() { return mandatory; }
    public int getMaxLength() { return maxLength; }
    public Class<?> getType() { return method.getReturnType(); }
    
    @Override
    public String toString() {
        return "PropertySpec{" +
                "name='" + name + '\'' +
                ", type=" + getType().getSimpleName() +
                ", editing=" + editing +
                '}';
    }
}
package org.causeway.poc.metamodel;

import java.lang.reflect.Method;

import org.causeway.poc.annotation.Action;

/**
 * Metamodel representation of an action.
 */
public class ActionSpec {
    
    private final Method method;
    private final String id;
    private final String name;
    private final Action.SemanticsOf semantics;
    private final boolean commandPublishing;
    
    public ActionSpec(Method method) {
        this.method = method;
        this.id = method.getName();
        this.name = deriveNameFromMethod(method);
        
        Action action = method.getAnnotation(Action.class);
        if (action != null) {
            this.semantics = action.semantics();
            this.commandPublishing = action.commandPublishing();
        } else {
            this.semantics = Action.SemanticsOf.NON_IDEMPOTENT;
            this.commandPublishing = false;
        }
    }
    
    private String deriveNameFromMethod(Method method) {
        return method.getName();
    }
    
    public Object invoke(Object target, Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke action", e);
        }
    }
    
    // Getters
    public Method getMethod() { return method; }
    public String getId() { return id; }
    public String getName() { return name; }
    public Action.SemanticsOf getSemantics() { return semantics; }
    public boolean isCommandPublishing() { return commandPublishing; }
    public Class<?> getReturnType() { return method.getReturnType(); }
    public Class<?>[] getParameterTypes() { return method.getParameterTypes(); }
    
    @Override
    public String toString() {
        return "ActionSpec{" +
                "name='" + name + '\'' +
                ", semantics=" + semantics +
                ", returnType=" + getReturnType().getSimpleName() +
                '}';
    }
}
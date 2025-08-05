package org.causeway.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an action (method) on a domain object.
 * Inspired by Apache Causeway's @Action annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
    
    /**
     * The semantics of this action.
     */
    SemanticsOf semantics() default SemanticsOf.NON_IDEMPOTENT;
    
    /**
     * Whether command should be published as events.
     */
    boolean commandPublishing() default false;
    
    enum SemanticsOf {
        SAFE,           // Query - no side effects
        IDEMPOTENT,     // Can be called multiple times safely  
        NON_IDEMPOTENT  // Has side effects
    }
}
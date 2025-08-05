package org.causeway.poc.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods/classes that require specific roles for access.
 * Simplified version inspired by secman's authorization system.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresRole {
    
    /**
     * The required role names.
     */
    String[] value();
    
    /**
     * Whether the user needs ALL roles or just ANY of the specified roles.
     */
    boolean requireAll() default false;
}
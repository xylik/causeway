package org.causeway.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a collection property on a domain object.
 * Inspired by Apache Causeway's @Collection annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Collection {
    
    /**
     * Whether this collection can be edited directly.
     */
    Editing editing() default Editing.ENABLED;
    
    enum Editing {
        ENABLED,
        DISABLED
    }
}
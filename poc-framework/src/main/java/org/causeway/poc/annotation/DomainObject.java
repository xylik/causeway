package org.causeway.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a domain object (entity or view model).
 * Inspired by Apache Causeway's @DomainObject annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainObject {
    
    /**
     * The nature of this domain object.
     */
    Nature nature() default Nature.ENTITY;
    
    /**
     * Name for the domain object type, used in the UI.
     */
    String objectType() default "";
    
    /**
     * Whether this object should be audited when changed.
     */
    boolean auditing() default false;
    
    enum Nature {
        ENTITY,
        VIEW_MODEL,
        MIXIN
    }
}
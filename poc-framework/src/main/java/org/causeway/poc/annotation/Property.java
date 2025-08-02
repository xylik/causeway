package org.causeway.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a property on a domain object.
 * Inspired by Apache Causeway's @Property annotation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
    
    /**
     * Whether this property can be edited directly.
     */
    Editing editing() default Editing.ENABLED;
    
    /**
     * Maximum length for string properties.
     */
    int maxLength() default -1;
    
    /**
     * Whether this property is mandatory.
     */
    boolean mandatory() default false;
    
    enum Editing {
        ENABLED,
        DISABLED
    }
}
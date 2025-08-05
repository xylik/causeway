package org.causeway.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a domain service.
 * Inspired by Apache Causeway's @DomainService annotation.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainService {
    
    /**
     * The nature of this service.
     */
    NatureOfService nature() default NatureOfService.DOMAIN;
    
    enum NatureOfService {
        DOMAIN,
        VIEW
    }
}
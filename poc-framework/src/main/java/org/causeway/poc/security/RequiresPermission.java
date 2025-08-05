package org.causeway.poc.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods/properties that require specific permissions for access.
 * Simplified version inspired by secman's permission system.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
    
    /**
     * The feature ID that the permission is for.
     * If empty, will be derived from the class and member name.
     */
    String featureId() default "";
    
    /**
     * The required permission mode.
     */
    PermissionMode mode() default PermissionMode.VIEWING;
}
package org.causeway.poc.security;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Security interceptor that checks permissions before method execution.
 * Simplified version inspired by secman's authorization framework.
 */
@Component
public class SecurityInterceptor {
    
    private final SecurityService securityService;
    
    public SecurityInterceptor(SecurityService securityService) {
        this.securityService = securityService;
    }
    
    /**
     * Check if the current user has access to execute the given method.
     */
    public void checkAccess(Object target, Method method) {
        // Check class-level security annotations
        checkClassLevelSecurity(target.getClass());
        
        // Check method-level security annotations
        checkMethodLevelSecurity(target, method);
    }
    
    /**
     * Check if the current user has access to view/modify a property.
     */
    public void checkPropertyAccess(Object target, String propertyName, boolean isWriteAccess) {
        try {
            // Try to find the getter method for the property
            String getterName = "get" + capitalize(propertyName);
            Method getter = target.getClass().getMethod(getterName);
            
            checkMethodLevelSecurity(target, getter);
            
            if (isWriteAccess) {
                // For write access, check if there's a setter method with additional restrictions
                try {
                    String setterName = "set" + capitalize(propertyName);
                    Method setter = target.getClass().getMethod(setterName, getter.getReturnType());
                    checkMethodLevelSecurity(target, setter);
                } catch (NoSuchMethodException e) {
                    // No setter found, check if we require CHANGING permission
                    checkFeaturePermission(target, propertyName, PermissionMode.CHANGING);
                }
            }
        } catch (NoSuchMethodException e) {
            // If no getter found, check generic feature permission
            PermissionMode mode = isWriteAccess ? PermissionMode.CHANGING : PermissionMode.VIEWING;
            checkFeaturePermission(target, propertyName, mode);
        }
    }
    
    private void checkClassLevelSecurity(Class<?> clazz) {
        RequiresRole roleAnnotation = clazz.getAnnotation(RequiresRole.class);
        if (roleAnnotation != null) {
            checkRoleRequirement(roleAnnotation);
        }
        
        RequiresPermission permissionAnnotation = clazz.getAnnotation(RequiresPermission.class);
        if (permissionAnnotation != null) {
            checkPermissionRequirement(clazz, null, permissionAnnotation);
        }
    }
    
    private void checkMethodLevelSecurity(Object target, Method method) {
        RequiresRole roleAnnotation = method.getAnnotation(RequiresRole.class);
        if (roleAnnotation != null) {
            checkRoleRequirement(roleAnnotation);
        }
        
        RequiresPermission permissionAnnotation = method.getAnnotation(RequiresPermission.class);
        if (permissionAnnotation != null) {
            checkPermissionRequirement(target.getClass(), method.getName(), permissionAnnotation);
        }
    }
    
    private void checkRoleRequirement(RequiresRole roleAnnotation) {
        if (roleAnnotation.requireAll()) {
            securityService.requireAllRoles(roleAnnotation.value());
        } else {
            securityService.requireAnyRole(roleAnnotation.value());
        }
    }
    
    private void checkPermissionRequirement(Class<?> clazz, String methodName, RequiresPermission permissionAnnotation) {
        String featureId = permissionAnnotation.featureId();
        if (featureId.isEmpty()) {
            // Generate feature ID from class and method name
            if (methodName != null) {
                featureId = securityService.generateFeatureId(clazz, methodName);
            } else {
                featureId = clazz.getSimpleName();
            }
        }
        
        securityService.requirePermission(featureId, permissionAnnotation.mode());
    }
    
    private void checkFeaturePermission(Object target, String featureName, PermissionMode mode) {
        String featureId = securityService.generateFeatureId(target, featureName);
        securityService.requirePermission(featureId, mode);
    }
    
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
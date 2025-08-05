package org.causeway.poc.viewer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.causeway.poc.objectmanager.ObjectManager;
import org.causeway.poc.metamodel.ObjectSpec;
import org.causeway.poc.security.SecurityService;
import org.causeway.poc.security.SecurityInterceptor;

/**
 * Web controller that generates dynamic UI for domain objects.
 * Inspired by Apache Causeway's Wicket viewer.
 * Now includes security integration with the secman-poc framework.
 */
@Controller
public class DomainObjectViewController {
    
    private final ObjectManager objectManager;
    private final SecurityService securityService;
    private final SecurityInterceptor securityInterceptor;
    
    public DomainObjectViewController(ObjectManager objectManager,
                                    SecurityService securityService,
                                    SecurityInterceptor securityInterceptor) {
        this.objectManager = objectManager;
        this.securityService = securityService;
        this.securityInterceptor = securityInterceptor;
    }
    
    /**
     * Home page showing all objects.
     */
    @GetMapping("/")
    public String home(Model model) {
        // Check if user is authenticated
        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }
        
        Map<String, java.util.List<ObjectInfo>> objectsByType = new HashMap<>();
        
        for (Object obj : objectManager.getAllObjects()) {
            try {
                // Check if user has permission to view this object type
                String featureId = obj.getClass().getSimpleName();
                if (securityService.canView(featureId)) {
                    String typeName = obj.getClass().getSimpleName();
                    String objectId = generateObjectId(obj);
                    ObjectInfo info = new ObjectInfo(obj, objectId);
                    objectsByType.computeIfAbsent(typeName, k -> new java.util.ArrayList<>()).add(info);
                }
            } catch (SecurityException e) {
                // User doesn't have permission to view this object - skip it
            }
        }
        
        model.addAttribute("objectsByType", objectsByType);
        model.addAttribute("currentUser", securityService.getCurrentUser());
        model.addAttribute("isAuthenticated", securityService.isAuthenticated());
        model.addAttribute("isAdmin", securityService.hasRole("admin"));
        return "home";
    }
    
    /**
     * Show a specific domain object.
     */
    @GetMapping("/object/{id}")
    public String showObject(@PathVariable String id, Model model) {
        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }
        
        return objectManager.findById(id)
            .map(obj -> {
                try {
                    // Check security for viewing this object
                    securityInterceptor.checkPropertyAccess(obj, "id", false);
                    
                    ObjectSpec spec = objectManager.getSpecification(obj);
                    model.addAttribute("object", obj);
                    model.addAttribute("spec", spec);
                    model.addAttribute("objectId", id);
                    model.addAttribute("currentUser", securityService.getCurrentUser());
                    model.addAttribute("securityService", securityService);
                    return "object-view";
                } catch (SecurityException e) {
                    model.addAttribute("error", "Access denied: " + e.getMessage());
                    return "redirect:/";
                }
            })
            .orElse("redirect:/");
    }
    
    /**
     * Invoke an action on an object.
     */
    @PostMapping("/object/{id}/action/{actionId}")
    public String invokeAction(@PathVariable String id, 
                             @PathVariable String actionId,
                             @RequestParam Map<String, String> params,
                             Model model) {
        
        if (!securityService.isAuthenticated()) {
            return "redirect:/login";
        }
        
        return objectManager.findById(id)
            .map(obj -> {
                try {
                    // Check security for invoking this action
                    java.lang.reflect.Method method = findActionMethod(obj, actionId);
                    if (method != null) {
                        securityInterceptor.checkAccess(obj, method);
                    }
                    
                    // Simple parameter handling - just pass strings for now
                    Object[] args = params.values().toArray();
                    Object result = objectManager.invokeAction(obj, actionId, args);
                    
                    if (result != null) {
                        model.addAttribute("actionResult", result);
                    }
                    
                    return "redirect:/object/" + id;
                } catch (SecurityException e) {
                    return "redirect:/object/" + id + "?error=Access denied: " + e.getMessage();
                } catch (Exception e) {
                    model.addAttribute("error", e.getMessage());
                    return "redirect:/object/" + id + "?error=" + e.getMessage();
                }
            })
            .orElse("redirect:/");
    }
    
    private String generateObjectId(Object obj) {
        return obj.getClass().getSimpleName() + "_" + obj.hashCode();
    }
    
    private java.lang.reflect.Method findActionMethod(Object obj, String actionId) {
        try {
            return obj.getClass().getMethod(actionId);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    // Helper class for template
    public static class ObjectInfo {
        private final Object object;
        private final String id;
        
        public ObjectInfo(Object object, String id) {
            this.object = object;
            this.id = id;
        }
        
        public Object getObject() { return object; }
        public String getId() { return id; }
        public String getTypeName() { return object.getClass().getSimpleName(); }
        public String getDescription() { return object.toString(); }
    }
}
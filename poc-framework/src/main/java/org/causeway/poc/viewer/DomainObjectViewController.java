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

/**
 * Web controller that generates dynamic UI for domain objects.
 * Inspired by Apache Causeway's Wicket viewer.
 */
@Controller
public class DomainObjectViewController {
    
    private final ObjectManager objectManager;
    
    public DomainObjectViewController(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }
    
    /**
     * Home page showing all objects.
     */
    @GetMapping("/")
    public String home(Model model) {
        Map<String, java.util.List<ObjectInfo>> objectsByType = new HashMap<>();
        
        for (Object obj : objectManager.getAllObjects()) {
            String typeName = obj.getClass().getSimpleName();
            String objectId = generateObjectId(obj);
            ObjectInfo info = new ObjectInfo(obj, objectId);
            objectsByType.computeIfAbsent(typeName, k -> new java.util.ArrayList<>()).add(info);
        }
        
        model.addAttribute("objectsByType", objectsByType);
        return "home";
    }
    
    /**
     * Show a specific domain object.
     */
    @GetMapping("/object/{id}")
    public String showObject(@PathVariable String id, Model model) {
        return objectManager.findById(id)
            .map(obj -> {
                ObjectSpec spec = objectManager.getSpecification(obj);
                model.addAttribute("object", obj);
                model.addAttribute("spec", spec);
                model.addAttribute("objectId", id);
                return "object-view";
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
        
        return objectManager.findById(id)
            .map(obj -> {
                try {
                    // Simple parameter handling - just pass strings for now
                    Object[] args = params.values().toArray();
                    Object result = objectManager.invokeAction(obj, actionId, args);
                    
                    if (result != null) {
                        model.addAttribute("actionResult", result);
                    }
                    
                    return "redirect:/object/" + id;
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
package org.causeway.poc.security;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Web controller for authentication.
 * Provides login/logout functionality for the secman-poc.
 */
@Controller
public class AuthController {
    
    private final SecurityService securityService;
    private final SecurityUserRepository userRepository;
    private final SecurityRoleRepository roleRepository;
    
    public AuthController(SecurityService securityService,
                         SecurityUserRepository userRepository,
                         SecurityRoleRepository roleRepository) {
        this.securityService = securityService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        return "security/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password,
                       RedirectAttributes redirectAttributes) {
        
        if (securityService.authenticate(username, password)) {
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/login";
        }
    }
    
    @PostMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        securityService.logout();
        redirectAttributes.addFlashAttribute("message", "You have been logged out");
        return "redirect:/login";
    }
    
    @GetMapping("/security")
    public String showSecurityAdmin(Model model) {
        try {
            securityService.requireRole("admin");
            
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("currentUser", securityService.getCurrentUser());
            
            return "security/admin";
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }
}
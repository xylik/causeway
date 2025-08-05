package org.causeway.poc.security;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes sample security data on application startup.
 * Creates default users, roles, and permissions for demonstration.
 */
@Component
public class SecurityDataInitializer implements ApplicationRunner {
    
    private final SecurityUserRepository userRepository;
    private final SecurityRoleRepository roleRepository;
    private final SecurityPermissionRepository permissionRepository;
    
    public SecurityDataInitializer(SecurityUserRepository userRepository,
                                  SecurityRoleRepository roleRepository,
                                  SecurityPermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeRoles();
        initializeUsers();
        initializePermissions();
    }
    
    private void initializeRoles() {
        // Create standard roles
        roleRepository.createRole("admin", "System Administrator - Full access to all features");
        roleRepository.createRole("user", "Regular User - Basic access to domain features");
        roleRepository.createRole("guest", "Guest User - Read-only access");
        roleRepository.createRole("customer-manager", "Customer Manager - Full access to customer management");
        roleRepository.createRole("order-manager", "Order Manager - Full access to order management");
    }
    
    private void initializeUsers() {
        // Create admin user
        SecurityUser admin = userRepository.createUser("admin", "System Administrator", "admin@example.com");
        SecurityRole adminRole = roleRepository.findByName("admin").orElseThrow();
        admin.addRole(adminRole);
        userRepository.save(admin);
        
        // Create regular user
        SecurityUser user = userRepository.createUser("user", "Regular User", "user@example.com");
        SecurityRole userRole = roleRepository.findByName("user").orElseThrow();
        user.addRole(userRole);
        userRepository.save(user);
        
        // Create guest user
        SecurityUser guest = userRepository.createUser("guest", "Guest User", "guest@example.com");
        SecurityRole guestRole = roleRepository.findByName("guest").orElseThrow();
        guest.addRole(guestRole);
        userRepository.save(guest);
        
        // Create customer manager
        SecurityUser customerManager = userRepository.createUser("cmgr", "Customer Manager", "cmgr@example.com");
        SecurityRole customerMgrRole = roleRepository.findByName("customer-manager").orElseThrow();
        customerManager.addRole(customerMgrRole);
        customerManager.addRole(userRole); // Also has basic user privileges
        userRepository.save(customerManager);
        
        // Create order manager
        SecurityUser orderManager = userRepository.createUser("omgr", "Order Manager", "omgr@example.com");
        SecurityRole orderMgrRole = roleRepository.findByName("order-manager").orElseThrow();
        orderManager.addRole(orderMgrRole);
        orderManager.addRole(userRole); // Also has basic user privileges
        userRepository.save(orderManager);
    }
    
    private void initializePermissions() {
        SecurityRole adminRole = roleRepository.findByName("admin").orElseThrow();
        SecurityRole userRole = roleRepository.findByName("user").orElseThrow();
        SecurityRole guestRole = roleRepository.findByName("guest").orElseThrow();
        SecurityRole customerMgrRole = roleRepository.findByName("customer-manager").orElseThrow();
        SecurityRole orderMgrRole = roleRepository.findByName("order-manager").orElseThrow();
        
        // Admin has full access to everything
        permissionRepository.grantPermission(adminRole, "Customer", PermissionMode.CHANGING);
        permissionRepository.grantPermission(adminRole, "Order", PermissionMode.CHANGING);
        permissionRepository.grantPermission(adminRole, "SecurityUser", PermissionMode.CHANGING);
        permissionRepository.grantPermission(adminRole, "SecurityRole", PermissionMode.CHANGING);
        permissionRepository.grantPermission(adminRole, "SecurityPermission", PermissionMode.CHANGING);
        
        // Regular users can view and modify basic domain objects
        permissionRepository.grantPermission(userRole, "Customer", PermissionMode.VIEWING);
        permissionRepository.grantPermission(userRole, "Order", PermissionMode.VIEWING);
        
        // Guests can only view basic domain objects
        permissionRepository.grantPermission(guestRole, "Customer", PermissionMode.VIEWING);
        permissionRepository.grantPermission(guestRole, "Order", PermissionMode.VIEWING);
        
        // Customer managers have full access to customers
        permissionRepository.grantPermission(customerMgrRole, "Customer", PermissionMode.CHANGING);
        permissionRepository.grantPermission(customerMgrRole, "Customer.name", PermissionMode.CHANGING);
        permissionRepository.grantPermission(customerMgrRole, "Customer.email", PermissionMode.CHANGING);
        permissionRepository.grantPermission(customerMgrRole, "Customer.createOrder", PermissionMode.CHANGING);
        
        // Order managers have full access to orders
        permissionRepository.grantPermission(orderMgrRole, "Order", PermissionMode.CHANGING);
        permissionRepository.grantPermission(orderMgrRole, "Order.product", PermissionMode.CHANGING);
        permissionRepository.grantPermission(orderMgrRole, "Order.quantity", PermissionMode.CHANGING);
        permissionRepository.grantPermission(orderMgrRole, "Order.ship", PermissionMode.CHANGING);
        permissionRepository.grantPermission(orderMgrRole, "Order.cancel", PermissionMode.CHANGING);
    }
}
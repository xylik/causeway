package org.causeway.poc.demo;

import java.util.ArrayList;
import java.util.List;

import org.causeway.poc.annotation.Action;
import org.causeway.poc.annotation.Collection;
import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;
import org.causeway.poc.security.RequiresPermission;
import org.causeway.poc.security.RequiresRole;
import org.causeway.poc.security.PermissionMode;

/**
 * Sample Customer domain object demonstrating the framework features.
 * Security: Requires user role to view, customer-manager role to modify.
 */
@DomainObject(nature = DomainObject.Nature.ENTITY, objectType = "Customer")
@RequiresRole("user") // Basic user role required to access customer data
public class Customer {
    
    private String name;
    private String email;
    private String phone;
    private List<Order> orders = new ArrayList<>();
    private boolean active = true;
    
    public Customer() {
    }
    
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    @Property(mandatory = true, maxLength = 100)
    @RequiresPermission(mode = PermissionMode.VIEWING)
    public String getName() {
        return name;
    }
    
    @RequiresPermission(mode = PermissionMode.CHANGING)
    public void setName(String name) {
        this.name = name;
    }
    
    @Property(mandatory = true, maxLength = 150)
    @RequiresPermission(mode = PermissionMode.VIEWING)
    public String getEmail() {
        return email;
    }
    
    @RequiresPermission(mode = PermissionMode.CHANGING)
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Property(maxLength = 20)
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Property(editing = Property.Editing.DISABLED)
    public boolean isActive() {
        return active;
    }
    
    @Collection(editing = Collection.Editing.DISABLED)
    public List<Order> getOrders() {
        return orders;
    }
    
    @Action(semantics = Action.SemanticsOf.NON_IDEMPOTENT, commandPublishing = true)
    @RequiresRole("customer-manager") // Only customer managers can create orders
    public Order placeOrder(String description, double amount) {
        Order order = new Order(description, amount, this);
        orders.add(order);
        return order;
    }
    
    @Action(semantics = Action.SemanticsOf.IDEMPOTENT)
    @RequiresRole("admin") // Only admins can deactivate customers
    public Customer deactivate() {
        this.active = false;
        return this;
    }
    
    @Action(semantics = Action.SemanticsOf.IDEMPOTENT)
    @RequiresRole("admin") // Only admins can activate customers
    public Customer activate() {
        this.active = true;
        return this;
    }
    
    @Action(semantics = Action.SemanticsOf.SAFE)
    public int getOrderCount() {
        return orders.size();
    }
    
    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
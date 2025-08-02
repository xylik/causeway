package org.causeway.poc.demo;

import java.time.LocalDateTime;

import org.causeway.poc.annotation.Action;
import org.causeway.poc.annotation.DomainObject;
import org.causeway.poc.annotation.Property;

/**
 * Sample Order domain object.
 */
@DomainObject(nature = DomainObject.Nature.ENTITY, objectType = "Order")
public class Order {
    
    private String description;
    private double amount;
    private Customer customer;
    private LocalDateTime orderDate;
    private OrderStatus status;
    
    public Order() {
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.PENDING;
    }
    
    public Order(String description, double amount, Customer customer) {
        this();
        this.description = description;
        this.amount = amount;
        this.customer = customer;
    }
    
    @Property(mandatory = true, maxLength = 200)
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Property(mandatory = true)
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Property(editing = Property.Editing.DISABLED)
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    @Property(editing = Property.Editing.DISABLED)
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    
    @Property
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    @Action(semantics = Action.SemanticsOf.IDEMPOTENT)
    public Order complete() {
        this.status = OrderStatus.COMPLETED;
        return this;
    }
    
    @Action(semantics = Action.SemanticsOf.IDEMPOTENT)
    public Order cancel() {
        this.status = OrderStatus.CANCELLED;
        return this;
    }
    
    @Override
    public String toString() {
        return description + " - $" + amount + " (" + status + ")";
    }
    
    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}
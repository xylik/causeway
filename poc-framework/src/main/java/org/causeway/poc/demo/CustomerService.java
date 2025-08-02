package org.causeway.poc.demo;

import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import org.causeway.poc.annotation.DomainService;
import org.causeway.poc.events.ActionInvocationEvent;
import org.causeway.poc.objectmanager.ObjectManager;

/**
 * Sample domain service demonstrating repository pattern and event handling.
 */
@Service
@DomainService(nature = DomainService.NatureOfService.DOMAIN)
public class CustomerService {
    
    private final ObjectManager objectManager;
    
    public CustomerService(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }
    
    public String createCustomer(String name, String email) {
        Customer customer = new Customer(name, email);
        return objectManager.persist(customer);
    }
    
    public List<Customer> getAllCustomers() {
        return objectManager.findAll(Customer.class)
                .stream()
                .map(obj -> (Customer) obj)
                .toList();
    }
    
    public List<Order> getAllOrders() {
        return objectManager.findAll(Order.class)
                .stream()
                .map(obj -> (Order) obj)
                .toList();
    }
    
    /**
     * Event listener demonstrating the event bus functionality.
     */
    @EventListener
    public void handleActionInvocation(ActionInvocationEvent event) {
        if (event.getPhase() == ActionInvocationEvent.EventPhase.EXECUTED) {
            System.out.println("Action executed: " + event.getActionId() + 
                             " on " + event.getSource().getClass().getSimpleName());
        }
    }
}
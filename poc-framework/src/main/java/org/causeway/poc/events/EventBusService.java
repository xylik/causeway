package org.causeway.poc.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Service for publishing domain events.
 * Uses Spring's ApplicationEventPublisher under the hood.
 */
@Service
public class EventBusService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public EventBusService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    /**
     * Publish a domain event.
     */
    public void publish(DomainEvent event) {
        eventPublisher.publishEvent(event);
    }
    
    /**
     * Publish action invocation events for all phases.
     */
    public Object publishActionInvocation(Object source, String actionId, Object[] arguments, 
                                        java.util.function.Supplier<Object> actionExecution) {
        
        // VALIDATE phase
        ActionInvocationEvent validateEvent = new ActionInvocationEvent(source, actionId, arguments, DomainEvent.EventPhase.VALIDATE);
        publish(validateEvent);
        
        // EXECUTING phase  
        ActionInvocationEvent executingEvent = new ActionInvocationEvent(source, actionId, arguments, DomainEvent.EventPhase.EXECUTING);
        publish(executingEvent);
        
        // Execute the action
        Object result = actionExecution.get();
        
        // EXECUTED phase
        ActionInvocationEvent executedEvent = new ActionInvocationEvent(source, actionId, arguments, DomainEvent.EventPhase.EXECUTED);
        executedEvent.setReturnValue(result);
        publish(executedEvent);
        
        return result;
    }
    
    /**
     * Publish property change events.
     */
    public void publishPropertyChange(Object source, String propertyId, Object oldValue, Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(source, propertyId, oldValue, newValue, DomainEvent.EventPhase.EXECUTED);
        publish(event);
    }
}
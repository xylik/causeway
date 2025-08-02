package org.causeway.poc.events;

/**
 * Base class for all domain events.
 * Inspired by Apache Causeway's domain events system.
 */
public abstract class DomainEvent {
    
    private final Object source;
    private final EventPhase phase;
    
    public DomainEvent(Object source, EventPhase phase) {
        this.source = source;
        this.phase = phase;
    }
    
    public Object getSource() {
        return source;
    }
    
    public EventPhase getPhase() {
        return phase;
    }
    
    public enum EventPhase {
        VALIDATE,   // Before execution - can veto
        EXECUTING,  // About to execute
        EXECUTED    // After execution
    }
}
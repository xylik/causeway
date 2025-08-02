package org.causeway.poc.events;

/**
 * Event published when a property value changes.
 */
public class PropertyChangeEvent extends DomainEvent {
    
    private final String propertyId;
    private final Object oldValue;
    private final Object newValue;
    
    public PropertyChangeEvent(Object source, String propertyId, Object oldValue, Object newValue, EventPhase phase) {
        super(source, phase);
        this.propertyId = propertyId;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
    
    public String getPropertyId() {
        return propertyId;
    }
    
    public Object getOldValue() {
        return oldValue;
    }
    
    public Object getNewValue() {
        return newValue;
    }
}
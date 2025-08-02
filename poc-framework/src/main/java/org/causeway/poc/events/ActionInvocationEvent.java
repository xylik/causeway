package org.causeway.poc.events;

/**
 * Event published when an action is invoked.
 */
public class ActionInvocationEvent extends DomainEvent {
    
    private final String actionId;
    private final Object[] arguments;
    private Object returnValue;
    
    public ActionInvocationEvent(Object source, String actionId, Object[] arguments, EventPhase phase) {
        super(source, phase);
        this.actionId = actionId;
        this.arguments = arguments;
    }
    
    public String getActionId() {
        return actionId;
    }
    
    public Object[] getArguments() {
        return arguments;
    }
    
    public Object getReturnValue() {
        return returnValue;
    }
    
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
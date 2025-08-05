# Causeway PoC Framework

A proof-of-concept framework demonstrating the key technical ideas implemented in Apache Causeway.

## Overview

This reduced framework showcases the core architectural patterns and concepts from Apache Causeway:

### Key Technical Ideas Demonstrated

1. **Domain-Driven Design with Annotations**
   - `@DomainObject` for marking entities and view models
   - `@Property` for object properties with metadata
   - `@Action` for business operations with semantics
   - `@Collection` for object collections
   - `@DomainService` for services

2. **Dynamic Metamodel**
   - Runtime introspection of domain objects using reflection
   - `ObjectSpec`, `PropertySpec`, `ActionSpec`, `CollectionSpec` classes
   - Annotation-driven configuration

3. **Event-Driven Architecture**
   - Internal event bus using Spring's `ApplicationEventPublisher`
   - Domain events for action invocations and property changes
   - Event phases: VALIDATE, EXECUTING, EXECUTED

4. **Object Lifecycle Management**
   - `ObjectManager` for persisting and retrieving domain objects
   - Simple in-memory repository implementation
   - Object identification and lookup

5. **Dynamic UI Generation**
   - Web viewer that generates UI from domain object metadata
   - Thymeleaf templates for property tables, action buttons, collections
   - Bootstrap styling for responsive design

6. **Service Layer**
   - `CustomerService` demonstrating repository pattern
   - Event listeners for cross-cutting concerns

## Architecture

```
poc-framework/
├── annotation/          # Domain annotations (@DomainObject, @Property, etc.)
├── metamodel/           # Metamodel classes (ObjectSpec, PropertySpec, etc.)
├── events/              # Event system (DomainEvent, EventBusService)
├── objectmanager/       # Object lifecycle management
├── viewer/              # Web UI generation
└── demo/                # Sample domain objects (Customer, Order)
```

## Key Features Demonstrated

- **Annotation-driven configuration**: Domain objects are configured using annotations rather than XML or code
- **Dynamic UI generation**: The web interface is generated automatically from domain object metadata
- **Event publishing**: Actions trigger events that can be subscribed to by other services
- **Metamodel introspection**: The framework builds a runtime model of domain objects
- **Type-safe operations**: All operations are checked against the metamodel

## Sample Domain Model

The demo includes:
- `Customer` entity with properties (name, email, phone) and actions (placeOrder, activate/deactivate)
- `Order` entity with properties and lifecycle actions (complete, cancel)
- `CustomerService` for managing customer operations

## Running the Framework

```bash
cd poc-framework
mvn spring-boot:run
```

Then visit http://localhost:8080 to see the dynamically generated UI.

## Differences from Full Apache Causeway

This is a minimal proof-of-concept focused on demonstrating core ideas:

- **Simplified**: No persistence layer, security, or complex layouts
- **In-memory**: Objects stored in memory rather than database
- **Basic UI**: Simple Bootstrap UI vs. full Wicket components
- **Limited features**: Only core annotation processing and event publishing

## Technical Architecture Patterns

1. **Metamodel Pattern**: Runtime introspection and metadata management
2. **Event Sourcing Pattern**: Domain events for decoupling
3. **Repository Pattern**: Object lifecycle management
4. **MVC Pattern**: Separation of domain, service, and presentation layers
5. **Annotation-driven Configuration**: Declarative metadata over imperative code

This demonstrates how Apache Causeway's sophisticated architecture can be understood through these fundamental patterns implemented in a simpler form.
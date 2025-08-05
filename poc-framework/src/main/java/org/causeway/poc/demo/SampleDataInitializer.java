package org.causeway.poc.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initializes sample data when the application starts.
 */
@Component
public class SampleDataInitializer implements CommandLineRunner {
    
    private final CustomerService customerService;
    
    public SampleDataInitializer(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample customers
        String customer1Id = customerService.createCustomer("John Doe", "john.doe@example.com");
        String customer2Id = customerService.createCustomer("Jane Smith", "jane.smith@example.com");
        String customer3Id = customerService.createCustomer("Bob Johnson", "bob.johnson@example.com");
        
        System.out.println("Sample data initialized:");
        System.out.println("- Created customer: " + customer1Id);
        System.out.println("- Created customer: " + customer2Id);  
        System.out.println("- Created customer: " + customer3Id);
        System.out.println("Visit http://localhost:8080 to see the generated UI");
    }
}
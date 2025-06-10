package vn.utc.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.entity.*;
import vn.utc.service.service.*;
import vn.utc.service.dtos.StaffRequest;
import vn.utc.service.dtos.RegisterRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializationService implements ApplicationRunner {

    @PersistenceContext
    private EntityManager entityManager;

    // Service dependencies for all operations
    private final RoleService roleService;
    private final SparePartService sparePartService;
    private final UserService userService;
    private final StaffService staffService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final AppointmentService appointmentService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== DATA INITIALIZATION SERVICE STARTED ===");
        log.info("Starting data initialization...");
        
        try {
            // Check if we're in development mode where tables might be created by Hibernate
            boolean isDevelopmentMode = checkIfDevelopmentMode();
            log.info("Development mode detected: {}", isDevelopmentMode);
            
            if (isDevelopmentMode) {
                // In development mode, wait a bit for Hibernate to potentially create tables
                log.info("Waiting for Hibernate to potentially create tables...");
                Thread.sleep(2000); // Wait 2 seconds
            }
            
            // Check if database schema is ready
            if (!isDatabaseSchemaReady()) {
                log.warn("Database schema is not ready. Skipping data initialization.");
                log.warn("Please ensure the database schema is created before running this service.");
                return;
            }
            
//            log.info("Truncating all tables before initialization...");
//            truncateAllTables();
            
            log.info("Testing database connection...");
            log.info("RoleService: {}", roleService);
            log.info("SparePartService: {}", sparePartService);
            log.info("UserService: {}", userService);
            
//            log.info("Starting role initialization...");
//            initializeRoles();
            
//            log.info("Starting spare parts initialization...");
//            initializeSpareParts();
//
            // log.info("Starting admin and manager initialization...");
            // initializeAdminAndManagerUsers();
            
//            log.info("Starting user initialization...");
//            initializeSampleUsers();
            
//            log.info("Starting staff initialization...");
//            initializeSampleStaff();
//
//            log.info("Starting customer initialization...");
//            initializeSampleCustomers();
            
//            log.info("Starting vehicle initialization...");
//            initializeSampleVehicles();
////
//            log.info("Starting appointment initialization...");
//            initializeSampleAppointments();
            
            log.info("=== DATA INITIALIZATION COMPLETED SUCCESSFULLY ===");
        } catch (Exception e) {
            log.error("Error during data initialization: ", e);
            throw e;
        }
    }

    private boolean checkIfDevelopmentMode() {
        try {
            // Check if we're running with dev profile or if ddl-auto is set to create-drop
            String activeProfile = System.getProperty("spring.profiles.active");
            if (activeProfile != null && activeProfile.contains("dev")) {
                return true;
            }
            
            // Also check if we can detect development mode from environment
            String springProfilesActive = System.getenv("SPRING_PROFILES_ACTIVE");
            if (springProfilesActive != null && springProfilesActive.contains("dev")) {
                return true;
            }
            
            return false;
        } catch (Exception e) {
            log.warn("Error checking development mode: {}", e.getMessage());
            return false;
        }
    }

    private boolean isDatabaseSchemaReady() {
        try {
            // Try to access the database to see if schema is ready
            // Check if at least one table exists
            boolean anyTableExists = checkTableExists("users") || 
                                   checkTableExists("roles") || 
                                   checkTableExists("appointments") ||
                                   checkTableExists("customers") ||
                                   checkTableExists("vehicles") ||
                                   checkTableExists("staff") ||
                                   checkTableExists("spare_parts");
            
            if (!anyTableExists) {
                log.warn("No database tables found. Schema may not be created yet.");
                return false;
            }
            
            // Try to execute a simple query to test connection
            try {
                // Test with a simple service call
                roleService.findByName("ADMIN");
                log.info("Database schema is ready.");
                return true;
            } catch (Exception e) {
                log.warn("Database schema validation failed: {}", e.getMessage());
                return false;
            }
        } catch (Exception e) {
            log.error("Error checking database schema readiness: {}", e.getMessage());
            return false;
        }
    }

    public void truncateAllTables() {
        log.info("Checking and truncating tables if they exist...");
        
        try {
            // Check if tables exist before trying to truncate
            boolean appointmentsExists = checkTableExists("appointments");
            boolean vehiclesExists = checkTableExists("vehicles");
            boolean customersExists = checkTableExists("customers");
            boolean staffExists = checkTableExists("staff");
            boolean usersExists = checkTableExists("users");
            boolean sparePartsExists = checkTableExists("spare_parts");
            boolean rolesExists = checkTableExists("roles");
            
            log.info("Table existence check - appointments: {}, vehicles: {}, customers: {}, staff: {}, users: {}, spare_parts: {}, roles: {}", 
                appointmentsExists, vehiclesExists, customersExists, staffExists, usersExists, sparePartsExists, rolesExists);
            
            // If no tables exist, skip truncation
            if (!appointmentsExists && !vehiclesExists && !customersExists && !staffExists && !usersExists && !sparePartsExists && !rolesExists) {
                log.info("No tables exist yet, skipping truncation. Tables will be created by Hibernate.");
                return;
            }
            
            // Disable foreign key checks temporarily (PostgreSQL syntax)
            //entityManager.createNativeQuery("SET session_replication_role = replica").executeUpdate();
            
            // Truncate tables in reverse dependency order (PostgreSQL syntax) - only if they exist
            if (appointmentsExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE appointments RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated appointments table");
            }
            
            if (vehiclesExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE vehicles RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated vehicles table");
            }
            
            if (customersExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE customers RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated customers table");
            }
            
            if (staffExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE staff RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated staff table");
            }
            
            if (usersExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE users RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated users table");
            }
            
            if (sparePartsExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE spare_parts RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated spare_parts table");
            }
            
            if (rolesExists) {
                entityManager.createNativeQuery("TRUNCATE TABLE roles RESTART IDENTITY CASCADE").executeUpdate();
                log.info("Truncated roles table");
            }
            
            // Re-enable foreign key checks (PostgreSQL syntax)
            //entityManager.createNativeQuery("SET session_replication_role = DEFAULT").executeUpdate();
            
            log.info("All existing tables truncated successfully");
        } catch (Exception e) {
            log.error("Error truncating tables: ", e);
            // Re-enable foreign key checks in case of error
            try {
                //entityManager.createNativeQuery("SET session_replication_role = DEFAULT").executeUpdate();
            } catch (Exception ex) {
                log.error("Error re-enabling foreign key checks: ", ex);
            }
            throw e;
        }
    }
    
    private boolean checkTableExists(String tableName) {
        try {
            // Use PostgreSQL-specific query to check if table exists
            String sql = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = ?)";
            Object result = entityManager.createNativeQuery(sql)
                .setParameter(1, tableName)
                .getSingleResult();
            return result instanceof Boolean ? (Boolean) result : false;
        } catch (Exception e) {
            log.warn("Error checking if table {} exists: {}", tableName, e.getMessage());
            return false;
        }
    }

    public void initializeRoles() {
        log.info("Initializing roles...");
        
        List<Role> roles = List.of(
            createRole("ADMIN", "Administrator with full access"),
            createRole("MANAGER", "Manager with management access"),
            createRole("STAFF", "Staff member with limited access"),
            createRole("CUSTOMER", "Customer with basic access")
        );
        
        roleService.saveAllForInitialization(roles);
        log.info("Roles initialized successfully.");
    }

    public void initializeSpareParts() {
        log.info("Initializing spare parts...");
        
        List<SparePart> spareParts = List.of(
            // Low stock items
            createSparePart("Oil Filter", "High-quality oil filter for engine protection", "Filters", 
                new BigDecimal("15.99"), new BigDecimal("8.50"), 2, 5, "A1-B2", "AutoParts Co."),
            createSparePart("Brake Pads", "Ceramic brake pads for optimal stopping power", "Brakes", 
                new BigDecimal("45.99"), new BigDecimal("25.00"), 1, 3, "C3-D4", "BrakeTech Inc."),
            createSparePart("Air Filter", "Premium air filter for better engine performance", "Filters", 
                new BigDecimal("12.99"), new BigDecimal("6.50"), 0, 4, "E5-F6", "FilterPro Ltd."),
            
            // Normal stock items
            createSparePart("Spark Plugs", "Iridium spark plugs for better ignition", "Ignition", 
                new BigDecimal("8.99"), new BigDecimal("4.50"), 15, 5, "G7-H8", "SparkTech Corp."),
            createSparePart("Windshield Wipers", "Premium windshield wiper blades", "Exterior", 
                new BigDecimal("22.99"), new BigDecimal("12.00"), 8, 3, "I9-J10", "WiperPro Inc."),
            createSparePart("Battery", "12V automotive battery with 3-year warranty", "Electrical", 
                new BigDecimal("89.99"), new BigDecimal("55.00"), 6, 2, "K11-L12", "BatteryMax Inc."),
            createSparePart("Tire Pressure Sensor", "TPMS sensor for accurate tire monitoring", "Tires", 
                new BigDecimal("35.99"), new BigDecimal("18.00"), 12, 4, "M13-N14", "TireTech Ltd."),
            createSparePart("Fuel Filter", "High-efficiency fuel filter for clean fuel delivery", "Filters", 
                new BigDecimal("18.99"), new BigDecimal("9.50"), 4, 3, "O15-P16", "FuelFilter Co."),
            createSparePart("Headlight Bulb", "LED headlight bulb for better visibility", "Lighting", 
                new BigDecimal("24.99"), new BigDecimal("12.50"), 10, 5, "Q17-R18", "LightPro Inc."),
            createSparePart("Radiator Hose", "Heavy-duty radiator hose for cooling system", "Cooling", 
                new BigDecimal("16.99"), new BigDecimal("8.00"), 7, 3, "S19-T20", "CoolingTech Ltd.")
        );
        
        // Save spare parts using service
        for (SparePart sparePart : spareParts) {
            sparePartService.save(sparePart);
        }
        log.info("Spare parts initialized successfully.");
    }

    public void initializeSampleUsers() {
        log.info("Initializing sample users...");
        
        // Create admin user
        User adminUser = createUser("admin", "admin@garage.com", "admin123", "ADMIN");
        userService.saveAllForInitialization(List.of(adminUser));
        
        // Create manager user
        User managerUser = createUser("manager", "manager@garage.com", "manager123", "MANAGER");
        userService.saveAllForInitialization(List.of(managerUser));
        
        // Create staff users
        User staff1User = createUser("johnsmith", "john.smith@garage.com", "staff123", "STAFF");
        User staff2User = createUser("mikejohnson", "mike.johnson@garage.com", "staff123", "STAFF");
        User staff3User = createUser("sarahwilson", "sarah.wilson@garage.com", "staff123", "STAFF");
        User staff4User = createUser("davidbrown", "david.brown@garage.com", "staff123", "STAFF");
        
        userService.saveAllForInitialization(List.of(staff1User, staff2User, staff3User, staff4User));
        
        // Create customer users
        User customer1User = createUser("johndoe", "john.doe@email.com", "customer123", "CUSTOMER");
        User customer2User = createUser("janedoe", "jane.doe@email.com", "customer123", "CUSTOMER");
        User customer3User = createUser("bobsmith", "bob.smith@email.com", "customer123", "CUSTOMER");
        User customer4User = createUser("alicejohnson", "alice.johnson@email.com", "customer123", "CUSTOMER");
        User customer5User = createUser("charliebrown", "charlie.brown@email.com", "customer123", "CUSTOMER");
        
        userService.saveAllForInitialization(List.of(customer1User, customer2User, customer3User, customer4User, customer5User));
        
        log.info("Sample users initialized successfully.");
    }

    public void initializeAdminAndManagerUsers() {
        log.info("Initializing admin and manager users...");
        
        List<StaffRequest> adminManagerRequests = List.of(
             createManagerRegisterRequest("manager", "manager@garage.com", "555-0002", "manager123",
                 "Garage", "Manager", "456 Manager Ave", "Springfield", "IL", "62701", "EMAIL")
        );
        
        // Create admin and manager users using AuthService.registerCustomer method
        for (StaffRequest request : adminManagerRequests) {
            try {
                staffService.createAdminAndManager(request, "MANAGER");
                log.info("Created staff member: {} {}", request.firstName(), request.lastName());
            } catch (Exception e) {
                log.error("Failed to create staff member {} {}: {}", 
                    request.firstName(), request.lastName(), e.getMessage());
            }
        }
        
        log.info("Admin and manager users initialized successfully.");
    }

    public StaffRequest createAdminRegisterRequest(String username, String email, String phone, String password,
                                                     String firstName, String lastName, String address, String city, 
                                                     String state, String zipCode, String preferredContactMethod) {
    return new StaffRequest(
        username,
        email,
        phone,
        password,
        firstName,
        lastName,
        address,
        city,
        state,
        zipCode,
        preferredContactMethod,
        "",
        "",
        LocalDate.now(),
        new BigDecimal(0));
    }

    public StaffRequest createManagerRegisterRequest(String username, String email, String phone, String password,
                                                       String firstName, String lastName, String address, String city, 
                                                       String state, String zipCode, String preferredContactMethod) {
        return new StaffRequest(username, email, phone, password, firstName, lastName, address, city, 
                                  state, zipCode, preferredContactMethod,"",
                "",
                LocalDate.now(),
                new BigDecimal(0));
    }

    public void initializeSampleStaff() {
        log.info("Initializing sample staff...");
        
        List<StaffRequest> staffRequests = List.of(
            createStaffRequest("johnsmith", "john.smith@garage.com", "555-0101", "staff123", 
                "John", "Smith", "123 Staff St", "Springfield", "IL", "62701", "Phone",
                "Senior Mechanic", "Engine Repair", LocalDate.of(2022, 1, 15), new BigDecimal("25.00")),
            createStaffRequest("mikejohnson", "mike.johnson@garage.com", "555-0102", "staff123", 
                "Mike", "Johnson", "456 Staff Ave", "Springfield", "IL", "62702", "Phone",
                "Mechanic", "Brake Systems", LocalDate.of(2022, 3, 20), new BigDecimal("22.00")),
            createStaffRequest("sarahwilson", "sarah.wilson@garage.com", "555-0103", "staff123", 
                "Sarah", "Wilson", "789 Staff Rd", "Springfield", "IL", "62703", "Email",
                "Technician", "Electrical Systems", LocalDate.of(2022, 6, 10), new BigDecimal("20.00")),
            createStaffRequest("davidbrown", "david.brown@garage.com", "555-0104", "staff123", 
                "David", "Brown", "321 Staff Dr", "Springfield", "IL", "62704", "Phone",
                "Apprentice", "General Maintenance", LocalDate.of(2023, 1, 5), new BigDecimal("18.00"))
        );
        
        // Create staff using StaffService.createStaff method
        for (StaffRequest staffRequest : staffRequests) {
            try {
                staffService.createStaff(staffRequest);
                log.info("Created staff member: {} {}", staffRequest.firstName(), staffRequest.lastName());
            } catch (Exception e) {
                log.error("Failed to create staff member {} {}: {}", 
                    staffRequest.firstName(), staffRequest.lastName(), e.getMessage());
            }
        }
        
        log.info("Sample staff initialization completed.");
    }

    public StaffRequest createStaffRequest(String username, String email, String phone, String password,
                                          String firstName, String lastName, String address, String city, 
                                          String state, String zipCode, String preferredContactMethod,
                                          String position, String specialization, LocalDate hireDate, 
                                          BigDecimal hourlyRate) {
        return new StaffRequest(username, email, phone, password, firstName, lastName, address, city, 
                               state, zipCode, preferredContactMethod, position, specialization, hireDate, hourlyRate);
    }

    public void initializeSampleCustomers() {
        log.info("Initializing sample customers...");
        
        List<RegisterRequest> customerRequests = List.of(
            createRegisterRequest("johndoe", "john.doe@email.com", "555-1001", "customer123", 
                "John", "Doe", "123 Main St", "Springfield", "IL", "62701", "EMAIL"),
            createRegisterRequest("janedoe", "jane.doe@email.com", "555-1002", "customer123", 
                "Jane", "Doe", "456 Oak Ave", "Springfield", "IL", "62702", "EMAIL"),
            createRegisterRequest("bobsmith", "bob.smith@email.com", "555-1003", "customer123", 
                "Bob", "Smith", "789 Pine Rd", "Chicago", "IL", "60601", "EMAIL"),
            createRegisterRequest("alicejohnson", "alice.johnson@email.com", "555-1004", "customer123", 
                "Alice", "Johnson", "321 Elm St", "Chicago", "IL", "60602", "EMAIL"),
            createRegisterRequest("charliebrown", "charlie.brown@email.com", "555-1005", "customer123", 
                "Charlie", "Brown", "654 Maple Dr", "Peoria", "IL", "61601", "EMAIL")
        );
        
        // Create customers using AuthService.registerCustomer method
        for (RegisterRequest customerRequest : customerRequests) {
            try {
                authService.registerCustomer(customerRequest);
                log.info("Created customer: {} {}", customerRequest.firstName(), customerRequest.lastName());
            } catch (Exception e) {
                log.error("Failed to create customer {} {}: {}", 
                    customerRequest.firstName(), customerRequest.lastName(), e.getMessage());
            }
        }
        
        log.info("Sample customers initialized successfully.");
    }

    public RegisterRequest createRegisterRequest(String username, String email, String phone, String password,
                                                String firstName, String lastName, String address, String city, 
                                                String state, String zipCode, String preferredContactMethod) {
        return new RegisterRequest(username, email, phone, password, firstName, lastName, address, city, 
                                  state, zipCode, preferredContactMethod, Set.of("CUSTOMER"));
    }

    public void initializeSampleVehicles() {
        log.info("Initializing sample vehicles...");
        
        User johnDoe = userService.findByUsernameForInitialization("johndoe").orElse(null);
        User janeDoe = userService.findByUsernameForInitialization("janedoe").orElse(null);
        User bobSmith = userService.findByUsernameForInitialization("bobsmith").orElse(null);
        User aliceJohnson = userService.findByUsernameForInitialization("alicejohnson").orElse(null);
        User charlieBrown = userService.findByUsernameForInitialization("charliebrown").orElse(null);
        
        if (johnDoe == null || janeDoe == null || bobSmith == null || aliceJohnson == null || charlieBrown == null) {
            log.warn("Some users not found, skipping vehicle initialization");
            return;
        }
        
        Customer johnDoeCustomer = customerService.findCustomerByUserForInitialization(johnDoe).orElse(null);
        Customer janeDoeCustomer = customerService.findCustomerByUserForInitialization(janeDoe).orElse(null);
        Customer bobSmithCustomer = customerService.findCustomerByUserForInitialization(bobSmith).orElse(null);
        Customer aliceJohnsonCustomer = customerService.findCustomerByUserForInitialization(aliceJohnson).orElse(null);
        Customer charlieBrownCustomer = customerService.findCustomerByUserForInitialization(charlieBrown).orElse(null);
        
        if (johnDoeCustomer == null || janeDoeCustomer == null || bobSmithCustomer == null || 
            aliceJohnsonCustomer == null || charlieBrownCustomer == null) {
            log.warn("Some customers not found, skipping vehicle initialization");
            log.warn("johnDoeCustomer: {}, janeDoeCustomer: {}, bobSmithCustomer: {}, aliceJohnsonCustomer: {}, charlieBrownCustomer: {}", 
                johnDoeCustomer, janeDoeCustomer, bobSmithCustomer, aliceJohnsonCustomer, charlieBrownCustomer);
            return;
        }
        
        List<Vehicle> vehicles = List.of(
            createVehicle(johnDoeCustomer, "Toyota", "Camry", 2020, "ABC123", "1HGBH41JXMN109186", "Silver", 45000),
            createVehicle(janeDoeCustomer, "Honda", "Civic", 2019, "DEF456", "2FMDK48C67BA12345", "Blue", 38000),
            createVehicle(bobSmithCustomer, "Ford", "F-150", 2021, "GHI789", "1FTEW1EG0JFA12345", "Red", 25000),
            createVehicle(aliceJohnsonCustomer, "Nissan", "Altima", 2018, "JKL012", "1N4AL3AP4JC123456", "White", 52000),
            createVehicle(charlieBrownCustomer, "Chevrolet", "Malibu", 2022, "MNO345", "1G1ZD5ST0LF123456", "Black", 15000)
        );
        
        vehicleService.saveAllForInitialization(vehicles);
        log.info("Sample vehicles initialized successfully.");
    }

    public void initializeSampleAppointments() {
        log.info("Initializing sample appointments...");
        
        User johnDoe = userService.findByUsernameForInitialization("johndoe").orElse(null);
        User janeDoe = userService.findByUsernameForInitialization("janedoe").orElse(null);
        User bobSmith = userService.findByUsernameForInitialization("bobsmith").orElse(null);
        
        if (johnDoe == null || janeDoe == null || bobSmith == null) {
            log.warn("Some users not found, skipping appointment initialization");
            return;
        }
        
        Customer johnDoeCustomer = customerService.findCustomerByUserForInitialization(johnDoe).orElse(null);
        Customer janeDoeCustomer = customerService.findCustomerByUserForInitialization(janeDoe).orElse(null);
        Customer bobSmithCustomer = customerService.findCustomerByUserForInitialization(bobSmith).orElse(null);
        
        if (johnDoeCustomer == null || janeDoeCustomer == null || bobSmithCustomer == null) {
            log.warn("Some customers not found, skipping appointment initialization");
            log.warn("johnDoeCustomer: {}, janeDoeCustomer: {}, bobSmithCustomer: {}", 
                johnDoeCustomer, janeDoeCustomer, bobSmithCustomer);
            return;
        }
        
        Vehicle johnVehicle = vehicleService.findVehiclesByCustomerIdForInitialization(johnDoeCustomer.getId()).stream().findFirst().orElse(null);
        Vehicle janeVehicle = vehicleService.findVehiclesByCustomerIdForInitialization(janeDoeCustomer.getId()).stream().findFirst().orElse(null);
        Vehicle bobVehicle = vehicleService.findVehiclesByCustomerIdForInitialization(bobSmithCustomer.getId()).stream().findFirst().orElse(null);
        
        if (johnVehicle == null || janeVehicle == null || bobVehicle == null) {
            log.warn("Some vehicles not found, skipping appointment initialization");
            log.warn("johnVehicle: {}, janeVehicle: {}, bobVehicle: {}", 
                johnVehicle, janeVehicle, bobVehicle);
            return;
        }
        
        Instant now = Instant.now();
        
        List<Appointment> appointments = List.of(
            createAppointment(johnVehicle, johnDoeCustomer, now.plusSeconds(3600), now.plusSeconds(7200), 
                "CONFIRMED", "Oil Change", "Regular oil change service"),
            createAppointment(janeVehicle, janeDoeCustomer, now.plusSeconds(7200), now.plusSeconds(10800), 
                "CONFIRMED", "Brake Service", "Brake pad replacement"),
            createAppointment(bobVehicle, bobSmithCustomer, now.minusSeconds(3600), now.plusSeconds(3600), 
                "IN_PROGRESS", "Engine Tune-up", "Complete engine tune-up service"),
            createAppointment(johnVehicle, johnDoeCustomer, now.minusSeconds(7200), now.minusSeconds(3600), 
                "COMPLETED", "Tire Rotation", "Tire rotation and balance"),
            createAppointment(janeVehicle, janeDoeCustomer, now.minusSeconds(10800), now.minusSeconds(7200), 
                "COMPLETED", "Air Filter Replacement", "Replace air filter and cabin filter")
        );
        
        appointmentService.saveAllForInitialization(appointments);
        log.info("Sample appointments initialized successfully.");
    }

    public Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());
        return role;
    }

    public SparePart createSparePart(String name, String description, String category,
                                     BigDecimal price, BigDecimal cost, Integer quantityInStock, 
                                     Integer minimumStockLevel, String location, String supplier) {
        SparePart sparePart = new SparePart();
        sparePart.setName(name);
        sparePart.setDescription(description);
        sparePart.setCategory(category);
        sparePart.setPrice(price);
        sparePart.setCost(cost);
        sparePart.setQuantityInStock(quantityInStock);
        sparePart.setMinimumStockLevel(minimumStockLevel);
        sparePart.setLocation(location);
        sparePart.setSupplier(supplier);
        sparePart.setCreatedAt(Instant.now());
        sparePart.setUpdatedAt(Instant.now());
        return sparePart;
    }

    public User createUser(String username, String email, String password, String roleName) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(roleName); // Use the exact role name as defined in initializeRoles
        user.setActive(true);
        user.setCreatedAt(Instant.now());
        
        // Find the role and add it to the user's roles set
        Role role = roleService.findByNameForInitialization(roleName).orElse(null);
        if (role != null) {
            user.getRoles().add(role);
        } else {
            log.warn("Role '{}' not found for user '{}'", roleName, username);
        }
        
        return user;
    }

    public Customer createCustomer(User user, String firstName, String lastName, String address,
                                  String city, String state, String zipCode, String preferredContactMethod) {
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setAddress(address);
        customer.setCity(city);
        customer.setState(state);
        customer.setZipCode(zipCode);
        customer.setPreferredContactMethod(preferredContactMethod);
        return customer;
    }

    public Vehicle createVehicle(Customer customer, String make, String model, Integer year,
                                String licensePlate, String vin, String color, Integer mileage) {
        Vehicle vehicle = new Vehicle();
        vehicle.setCustomer(customer);
        vehicle.setMake(make);
        vehicle.setModel(model);
        vehicle.setYear(year);
        vehicle.setLicensePlate(licensePlate);
        vehicle.setVin(vin);
        vehicle.setColor(color);
        vehicle.setMileage(mileage);
        vehicle.setRegistrationDate(Instant.now());
        return vehicle;
    }

    public Appointment createAppointment(Vehicle vehicle, Customer customer, Instant appointmentDate,
                                        Instant estimatedCompletion, String status, String serviceType, String description) {
        Appointment appointment = new Appointment();
        appointment.setVehicle(vehicle);
        appointment.setCustomer(customer);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setEstimatedCompletion(estimatedCompletion);
        appointment.setStatus(status);
        appointment.setServiceType(serviceType);
        appointment.setDescription(description);
        appointment.setCreatedAt(Instant.now());
        appointment.setUpdatedAt(Instant.now());
        return appointment;
    }
} 
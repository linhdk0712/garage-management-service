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
import java.util.Optional;


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
            
           log.info("Truncating all tables before initialization...");
           truncateAllTables();

            // Clear Hibernate session to avoid identity conflicts after truncation
            entityManager.clear();
            
            log.info("Testing database connection...");
            log.info("RoleService: {}", roleService);
            log.info("SparePartService: {}", sparePartService);
            log.info("UserService: {}", userService);
            
           log.info("Starting role initialization...");
           initializeRoles();
            
           log.info("Starting spare parts initialization...");
           initializeSpareParts();

            log.info("Starting admin and manager initialization...");
            initializeAdminAndManagerUsers();
            
           log.info("Starting user initialization...");
           initializeSampleUsers();
            
           log.info("Starting staff initialization...");
           initializeSampleStaff();

           log.info("Starting customer initialization...");
           initializeSampleCustomers();
            
           log.info("Starting vehicle initialization...");
           initializeSampleVehicles();

           log.info("Starting appointment initialization...");
           initializeSampleAppointments();
            
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
            createRole("RECEPTIONIST", "Receptionist with limited access"),
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
                new BigDecimal("16.99"), new BigDecimal("8.00"), 7, 3, "S19-T20", "CoolingTech Ltd."),
            // Additional items for pagination
            createSparePart("Alternator", "High output alternator", "Electrical", new BigDecimal("120.00"), new BigDecimal("80.00"), 5, 2, "U21-V22", "ElectroParts Inc."),
            createSparePart("Timing Belt", "Durable timing belt for long life", "Engine", new BigDecimal("60.00"), new BigDecimal("30.00"), 9, 3, "W23-X24", "BeltWorks Ltd."),
            createSparePart("Shock Absorber", "Gas-filled shock absorber", "Suspension", new BigDecimal("75.00"), new BigDecimal("40.00"), 11, 4, "Y25-Z26", "ShockMaster Co."),
            createSparePart("Clutch Kit", "Complete clutch replacement kit", "Transmission", new BigDecimal("150.00"), new BigDecimal("90.00"), 3, 2, "A27-B28", "ClutchPro Inc.")
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
        if (userService.findByUsernameForInitialization("admin").isEmpty()) {
            User adminUser = createUser("admin", "admin@garage.com", "admin123", "ADMIN");
            userService.saveAllForInitialization(List.of(adminUser));
        }
        
        // Create manager user
        if (userService.findByUsernameForInitialization("manager").isEmpty()) {
            User managerUser = createUser("manager", "manager@garage.com", "manager123", "MANAGER");
            userService.saveAllForInitialization(List.of(managerUser));
        }
        
        // Create staff users
        String[] staffUsernames = {"johnsmith", "mikejohnson", "sarahwilson", "davidbrown", "emilyclark", "kevinlee", "laurawhite", "robertking", "angelamartin", "stevenyoung"};
        List<User> staffUsers = new java.util.ArrayList<>();
        for (String username : staffUsernames) {
            if (userService.findByUsernameForInitialization(username).isEmpty()) {
                staffUsers.add(createUser(username, username.replace(".", "_") + "@garage.com", "staff123", "STAFF"));
            }
        }
        if (!staffUsers.isEmpty()) {
            userService.saveAllForInitialization(staffUsers);
        }
        
        // Create receptionist user
        if (userService.findByUsernameForInitialization("reception1").isEmpty()) {
            User receptionistUser = createUser("reception1", "reception1@garage.com", "reception123", "RECEPTIONIST");
            userService.saveAllForInitialization(List.of(receptionistUser));
        }
        
        // Create customer users
        String[] customerUsernames = {"johndoe", "janedoe", "bobsmith", "alicejohnson", "charliebrown", "lucasgray", "oliviamartin", "ethanlee", "sophiabaker", "masonhill", "avaadams"};
        List<User> customerUsers = new java.util.ArrayList<>();
        for (String username : customerUsernames) {
            if (userService.findByUsernameForInitialization(username).isEmpty()) {
                customerUsers.add(createUser(username, username + "@email.com", "customer123", "CUSTOMER"));
            }
        }
        if (!customerUsers.isEmpty()) {
            userService.saveAllForInitialization(customerUsers);
        }
        
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
                "Apprentice", "General Maintenance", LocalDate.of(2023, 1, 5), new BigDecimal("18.00")),
            createStaffRequest("emilyclark", "emily.clark@garage.com", "555-0105", "staff123", 
                "Emily", "Clark", "654 Staff Ln", "Springfield", "IL", "62705", "Email",
                "Receptionist", "Front Desk", LocalDate.of(2023, 2, 10), new BigDecimal("19.00")),
            createStaffRequest("kevinlee", "kevin.lee@garage.com", "555-0106", "staff123", 
                "Kevin", "Lee", "987 Staff Blvd", "Springfield", "IL", "62706", "Phone",
                "Technician", "Diagnostics", LocalDate.of(2023, 3, 15), new BigDecimal("21.00")),
            createStaffRequest("laurawhite", "laura.white@garage.com", "555-0107", "staff123", 
                "Laura", "White", "321 Staff Cir", "Springfield", "IL", "62707", "Email",
                "Mechanic", "Suspension", LocalDate.of(2023, 4, 20), new BigDecimal("22.50")),
            createStaffRequest("robertking", "robert.king@garage.com", "555-0108", "staff123", 
                "Robert", "King", "852 Staff Pkwy", "Springfield", "IL", "62708", "Phone",
                "Technician", "Transmission", LocalDate.of(2023, 5, 25), new BigDecimal("23.00")),
            createStaffRequest("angelamartin", "angela.martin@garage.com", "555-0109", "staff123", 
                "Angela", "Martin", "963 Staff Ave", "Springfield", "IL", "62709", "Email",
                "Receptionist", "Customer Service", LocalDate.of(2023, 6, 30), new BigDecimal("19.50")),
            createStaffRequest("stevenyoung", "steven.young@garage.com", "555-0110", "staff123", 
                "Steven", "Young", "147 Staff St", "Springfield", "IL", "62710", "Phone",
                "Technician", "Body Work", LocalDate.of(2023, 7, 5), new BigDecimal("20.50")),
            createStaffRequest("reception1", "reception1@garage.com", "555-0111", "reception123", 
                "Reception", "One", "258 Reception Rd", "Springfield", "IL", "62711", "Email",
                "Receptionist", "Front Desk", LocalDate.of(2023, 8, 10), new BigDecimal("19.00"))
        );
        
        List<Staff> missingStaff = new java.util.ArrayList<>();
        // Create staff using StaffService.createStaff method, only if username does not exist
        for (StaffRequest staffRequest : staffRequests) {
            Optional<User> userOpt = userService.findByUsernameForInitialization(staffRequest.username());
            if (userOpt.isEmpty()) {
                try {
                    staffService.createStaff(staffRequest);
                    log.info("Created staff member: {} {}", staffRequest.firstName(), staffRequest.lastName());
                } catch (Exception e) {
                    log.error("Failed to create staff member {} {}: {}", 
                        staffRequest.firstName(), staffRequest.lastName(), e.getMessage());
                }
            } else {
                // User exists, check if Staff exists
                User user = userOpt.get();
                Optional<Staff> staffOpt = staffService.findStaffByUserForInitialization(user);
                if (staffOpt.isEmpty()) {
                    try {
                        Staff staff = createStaff(
                            user,
                            staffRequest.firstName(),
                            staffRequest.lastName(),
                            staffRequest.position(),
                            staffRequest.specialization(),
                            staffRequest.hireDate(),
                            staffRequest.hourlyRate()
                        );
                        missingStaff.add(staff);
                        log.info("Prepared missing staff for user: {}", staffRequest.username());
                    } catch (Exception e) {
                        log.error("Failed to prepare missing staff for user {}: {}", staffRequest.username(), e.getMessage());
                    }
                } else {
                    log.info("Staff for user {} already exists, skipping.", staffRequest.username());
                }
            }
        }
        if (!missingStaff.isEmpty()) {
            staffService.saveAllForInitialization(missingStaff);
            log.info("Saved {} missing staff.", missingStaff.size());
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
                "Charlie", "Brown", "654 Maple Dr", "Peoria", "IL", "61601", "EMAIL"),
            createRegisterRequest("lucasgray", "lucas.gray@email.com", "555-1006", "customer123", 
                "Lucas", "Gray", "987 Cedar St", "Peoria", "IL", "61602", "EMAIL"),
            createRegisterRequest("oliviamartin", "olivia.martin@email.com", "555-1007", "customer123", 
                "Olivia", "Martin", "159 Spruce Ave", "Peoria", "IL", "61603", "EMAIL"),
            createRegisterRequest("ethanlee", "ethan.lee@email.com", "555-1008", "customer123", 
                "Ethan", "Lee", "753 Birch Rd", "Peoria", "IL", "61604", "EMAIL"),
            createRegisterRequest("sophiabaker", "sophia.baker@email.com", "555-1009", "customer123", 
                "Sophia", "Baker", "357 Walnut St", "Peoria", "IL", "61605", "EMAIL"),
            createRegisterRequest("masonhill", "mason.hill@email.com", "555-1010", "customer123", 
                "Mason", "Hill", "951 Poplar Dr", "Peoria", "IL", "61606", "EMAIL"),
            createRegisterRequest("avaadams", "ava.adams@email.com", "555-1011", "customer123", 
                "Ava", "Adams", "258 Willow Ln", "Peoria", "IL", "61607", "EMAIL")
        );
        
        List<Customer> missingCustomers = new java.util.ArrayList<>();
        // Create customers using AuthService.registerCustomer method, only if username does not exist
        for (RegisterRequest customerRequest : customerRequests) {
            Optional<User> userOpt = userService.findByUsernameForInitialization(customerRequest.username());
            if (userOpt.isEmpty()) {
                try {
                    authService.registerCustomer(customerRequest);
                    log.info("Created customer: {} {}", customerRequest.firstName(), customerRequest.lastName());
                } catch (Exception e) {
                    log.error("Failed to create customer {} {}: {}", 
                        customerRequest.firstName(), customerRequest.lastName(), e.getMessage());
                }
            } else {
                // User exists, check if Customer exists
                User user = userOpt.get();
                Optional<Customer> customerOpt = customerService.findCustomerByUserForInitialization(user);
                if (customerOpt.isEmpty()) {
                    try {
                        Customer customer = createCustomer(
                            user,
                            customerRequest.firstName(),
                            customerRequest.lastName(),
                            customerRequest.address(),
                            customerRequest.city(),
                            customerRequest.state(),
                            customerRequest.zipCode(),
                            customerRequest.preferredContactMethod()
                        );
                        missingCustomers.add(customer);
                        log.info("Prepared missing customer for user: {}", customerRequest.username());
                    } catch (Exception e) {
                        log.error("Failed to prepare missing customer for user {}: {}", customerRequest.username(), e.getMessage());
                    }
                } else {
                    log.info("Customer for user {} already exists, skipping.", customerRequest.username());
                }
            }
        }
        if (!missingCustomers.isEmpty()) {
            customerService.saveAllForInitialization(missingCustomers);
            log.info("Saved {} missing customers.", missingCustomers.size());
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
        User lucasGray = userService.findByUsernameForInitialization("lucasgray").orElse(null);
        User oliviaMartin = userService.findByUsernameForInitialization("oliviamartin").orElse(null);
        User ethanLee = userService.findByUsernameForInitialization("ethanlee").orElse(null);
        User sophiaBaker = userService.findByUsernameForInitialization("sophiabaker").orElse(null);
        User masonHill = userService.findByUsernameForInitialization("masonhill").orElse(null);
        User avaAdams = userService.findByUsernameForInitialization("avaadams").orElse(null);
        
        if (johnDoe == null || janeDoe == null || bobSmith == null || aliceJohnson == null || charlieBrown == null ||
            lucasGray == null || oliviaMartin == null || ethanLee == null || sophiaBaker == null || masonHill == null || avaAdams == null) {
            log.warn("Some users not found, skipping vehicle initialization");
            return;
        }
        
        Customer johnDoeCustomer = customerService.findCustomerByUserForInitialization(johnDoe).orElse(null);
        Customer janeDoeCustomer = customerService.findCustomerByUserForInitialization(janeDoe).orElse(null);
        Customer bobSmithCustomer = customerService.findCustomerByUserForInitialization(bobSmith).orElse(null);
        Customer aliceJohnsonCustomer = customerService.findCustomerByUserForInitialization(aliceJohnson).orElse(null);
        Customer charlieBrownCustomer = customerService.findCustomerByUserForInitialization(charlieBrown).orElse(null);
        Customer lucasGrayCustomer = customerService.findCustomerByUserForInitialization(lucasGray).orElse(null);
        Customer oliviaMartinCustomer = customerService.findCustomerByUserForInitialization(oliviaMartin).orElse(null);
        Customer ethanLeeCustomer = customerService.findCustomerByUserForInitialization(ethanLee).orElse(null);
        Customer sophiaBakerCustomer = customerService.findCustomerByUserForInitialization(sophiaBaker).orElse(null);
        Customer masonHillCustomer = customerService.findCustomerByUserForInitialization(masonHill).orElse(null);
        Customer avaAdamsCustomer = customerService.findCustomerByUserForInitialization(avaAdams).orElse(null);
        
        if (johnDoeCustomer == null || janeDoeCustomer == null || bobSmithCustomer == null || 
            aliceJohnsonCustomer == null || charlieBrownCustomer == null || lucasGrayCustomer == null ||
            oliviaMartinCustomer == null || ethanLeeCustomer == null || sophiaBakerCustomer == null ||
            masonHillCustomer == null || avaAdamsCustomer == null) {
            log.warn("Some customers not found, skipping vehicle initialization");
            log.warn("johnDoeCustomer: {}, janeDoeCustomer: {}, bobSmithCustomer: {}, aliceJohnsonCustomer: {}, charlieBrownCustomer: {}, lucasGrayCustomer: {}, oliviaMartinCustomer: {}, ethanLeeCustomer: {}, sophiaBakerCustomer: {}, masonHillCustomer: {}, avaAdamsCustomer: {}", 
                johnDoeCustomer, janeDoeCustomer, bobSmithCustomer, aliceJohnsonCustomer, charlieBrownCustomer, lucasGrayCustomer, oliviaMartinCustomer, ethanLeeCustomer, sophiaBakerCustomer, masonHillCustomer, avaAdamsCustomer);
            return;
        }
        
        List<Vehicle> vehicles = List.of(
            createVehicle(johnDoeCustomer, "Toyota", "Camry", 2020, "ABC123", "1HGBH41JXMN109186", "Silver", 45000),
            createVehicle(janeDoeCustomer, "Honda", "Civic", 2019, "DEF456", "2FMDK48C67BA12345", "Blue", 38000),
            createVehicle(bobSmithCustomer, "Ford", "F-150", 2021, "GHI789", "1FTEW1EG0JFA12345", "Red", 25000),
            createVehicle(aliceJohnsonCustomer, "Nissan", "Altima", 2018, "JKL012", "1N4AL3AP4JC123456", "White", 52000),
            createVehicle(charlieBrownCustomer, "Chevrolet", "Malibu", 2022, "MNO345", "1G1ZD5ST0LF123456", "Black", 15000),
            createVehicle(lucasGrayCustomer, "Hyundai", "Elantra", 2021, "PQR678", "KMHD84LF6JU123456", "Gray", 22000),
            createVehicle(oliviaMartinCustomer, "Kia", "Sorento", 2020, "STU901", "5XYKT3A19EG123456", "Green", 33000),
            createVehicle(ethanLeeCustomer, "Mazda", "CX-5", 2019, "VWX234", "JM3KFBCM6K1234567", "Blue", 41000),
            createVehicle(sophiaBakerCustomer, "Volkswagen", "Jetta", 2018, "YZA567", "3VWD17AJ5FM123456", "White", 47000),
            createVehicle(masonHillCustomer, "Subaru", "Outback", 2022, "BCD890", "4S4BSANC6J3321456", "Red", 12000),
            createVehicle(avaAdamsCustomer, "Jeep", "Wrangler", 2023, "EFG123", "1C4HJXDG7JW123456", "Yellow", 8000)
        );
        
        vehicleService.saveAllForInitialization(vehicles);
        log.info("Sample vehicles initialized successfully.");
    }

    public void initializeSampleAppointments() {
        log.info("Initializing sample appointments...");
        
        // Fetch all customers and vehicles for appointments
        List<User> users = List.of(
            userService.findByUsernameForInitialization("johndoe").orElse(null),
            userService.findByUsernameForInitialization("janedoe").orElse(null),
            userService.findByUsernameForInitialization("bobsmith").orElse(null),
            userService.findByUsernameForInitialization("alicejohnson").orElse(null),
            userService.findByUsernameForInitialization("charliebrown").orElse(null),
            userService.findByUsernameForInitialization("lucasgray").orElse(null),
            userService.findByUsernameForInitialization("oliviamartin").orElse(null),
            userService.findByUsernameForInitialization("ethanlee").orElse(null),
            userService.findByUsernameForInitialization("sophiabaker").orElse(null),
            userService.findByUsernameForInitialization("masonhill").orElse(null),
            userService.findByUsernameForInitialization("avaadams").orElse(null)
        );
        
        List<Customer> customers = users.stream()
            .map(u -> u == null ? null : customerService.findCustomerByUserForInitialization(u).orElse(null))
            .toList();
        
        List<Vehicle> vehicles = customers.stream()
            .map(c -> c == null ? null : vehicleService.findVehiclesByCustomerIdForInitialization(c.getId()).stream().findFirst().orElse(null))
            .toList();
        
        Instant now = Instant.now();
        
        List<Appointment> appointments = List.of(
            createAppointment(vehicles.get(0), customers.get(0), now.plusSeconds(3600), now.plusSeconds(7200), "CONFIRMED", "Oil Change", "Regular oil change service"),
            createAppointment(vehicles.get(1), customers.get(1), now.plusSeconds(7200), now.plusSeconds(10800), "CONFIRMED", "Brake Service", "Brake pad replacement"),
            createAppointment(vehicles.get(2), customers.get(2), now.minusSeconds(3600), now.plusSeconds(3600), "IN_PROGRESS", "Engine Tune-up", "Complete engine tune-up service"),
            createAppointment(vehicles.get(3), customers.get(3), now.minusSeconds(7200), now.minusSeconds(3600), "COMPLETED", "Tire Rotation", "Tire rotation and balance"),
            createAppointment(vehicles.get(4), customers.get(4), now.minusSeconds(10800), now.minusSeconds(7200), "COMPLETED", "Air Filter Replacement", "Replace air filter and cabin filter"),
            createAppointment(vehicles.get(5), customers.get(5), now.plusSeconds(14400), now.plusSeconds(18000), "CONFIRMED", "Battery Replacement", "Replace car battery"),
            createAppointment(vehicles.get(6), customers.get(6), now.plusSeconds(21600), now.plusSeconds(25200), "CONFIRMED", "Tire Change", "Change all tires"),
            createAppointment(vehicles.get(7), customers.get(7), now.plusSeconds(28800), now.plusSeconds(32400), "CONFIRMED", "Transmission Check", "Check transmission system"),
            createAppointment(vehicles.get(8), customers.get(8), now.plusSeconds(36000), now.plusSeconds(39600), "CONFIRMED", "Suspension Inspection", "Inspect suspension system"),
            createAppointment(vehicles.get(9), customers.get(9), now.plusSeconds(43200), now.plusSeconds(46800), "CONFIRMED", "AC Service", "Air conditioning system service"),
            createAppointment(vehicles.get(10), customers.get(10), now.plusSeconds(50400), now.plusSeconds(54000), "CONFIRMED", "Detailing", "Full car detailing")
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

    public Staff createStaff(User user, String firstName, String lastName, String position, String specialization, LocalDate hireDate, BigDecimal hourlyRate) {
        Staff staff = new Staff();
        staff.setUser(user);
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setPosition(position);
        staff.setSpecialization(specialization);
        staff.setHireDate(hireDate);
        staff.setHourlyRate(hourlyRate);
        return staff;
    }
} 
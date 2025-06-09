package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.entity.SparePart;
import vn.utc.service.repo.SparePartRepository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SparePartService {
    
    private final SparePartRepository sparePartRepository;
    
    /**
     * Find all spare parts that are below their minimum stock level
     * @return List of spare parts with low stock
     */
    public List<SparePart> findLowStockItems() {
        List<SparePart> allParts = sparePartRepository.findAll();
        
        // If no parts in database, return mock data for testing
        if (allParts.isEmpty()) {
            return createMockLowStockItems();
        }
        
        return allParts.stream()
                .filter(part -> part.getQuantityInStock() <= part.getMinimumStockLevel())
                .toList();
    }
    
    /**
     * Create mock low stock items for testing
     * @return List of mock spare parts with low stock
     */
    private List<SparePart> createMockLowStockItems() {
        SparePart part1 = new SparePart()
                .setId(1)
                .setName("Oil Filter")
                .setDescription("High-quality oil filter for engine protection")
                .setCategory("Filters")
                .setPrice(new java.math.BigDecimal("15.99"))
                .setCost(new java.math.BigDecimal("8.50"))
                .setQuantityInStock(2)
                .setMinimumStockLevel(5)
                .setLocation("A1-B2")
                .setSupplier("AutoParts Co.")
                .setCreatedAt(java.time.Instant.now())
                .setUpdatedAt(java.time.Instant.now());
                
        SparePart part2 = new SparePart()
                .setId(2)
                .setName("Brake Pads")
                .setDescription("Ceramic brake pads for optimal stopping power")
                .setCategory("Brakes")
                .setPrice(new java.math.BigDecimal("45.99"))
                .setCost(new java.math.BigDecimal("25.00"))
                .setQuantityInStock(1)
                .setMinimumStockLevel(3)
                .setLocation("C3-D4")
                .setSupplier("BrakeTech Inc.")
                .setCreatedAt(java.time.Instant.now())
                .setUpdatedAt(java.time.Instant.now());
                
        SparePart part3 = new SparePart()
                .setId(3)
                .setName("Air Filter")
                .setDescription("Premium air filter for better engine performance")
                .setCategory("Filters")
                .setPrice(new java.math.BigDecimal("12.99"))
                .setCost(new java.math.BigDecimal("6.50"))
                .setQuantityInStock(0)
                .setMinimumStockLevel(4)
                .setLocation("E5-F6")
                .setSupplier("FilterPro Ltd.")
                .setCreatedAt(java.time.Instant.now())
                .setUpdatedAt(java.time.Instant.now());
                
        return List.of(part1, part2, part3);
    }
    
    /**
     * Find a spare part by its ID
     * @param id The spare part ID
     * @return Optional containing the spare part if found
     */
    public Optional<SparePart> findById(Integer id) {
        return sparePartRepository.findById(id);
    }
    
    /**
     * Find all spare parts
     * @return List of all spare parts
     */
    public List<SparePart> findAll() {
        List<SparePart> allParts = sparePartRepository.findAll();
        
        // If no parts in database, return mock data for testing
        if (allParts.isEmpty()) {
            return createMockParts();
        }
        
        return allParts;
    }
    
    /**
     * Create mock spare parts for testing
     * @return List of mock spare parts
     */
    private List<SparePart> createMockParts() {
        List<SparePart> mockParts = new ArrayList<>();
        
        // Add the low stock items
        mockParts.addAll(createMockLowStockItems());
        
        // Add some normal stock items
        SparePart part4 = new SparePart()
                .setId(4)
                .setName("Spark Plugs")
                .setDescription("Iridium spark plugs for better ignition")
                .setCategory("Ignition")
                .setPrice(new java.math.BigDecimal("8.99"))
                .setCost(new java.math.BigDecimal("4.50"))
                .setQuantityInStock(15)
                .setMinimumStockLevel(5)
                .setLocation("G7-H8")
                .setSupplier("SparkTech Corp.")
                .setCreatedAt(java.time.Instant.now())
                .setUpdatedAt(java.time.Instant.now());
                
        SparePart part5 = new SparePart()
                .setId(5)
                .setName("Windshield Wipers")
                .setDescription("Premium windshield wiper blades")
                .setCategory("Exterior")
                .setPrice(new java.math.BigDecimal("22.99"))
                .setCost(new java.math.BigDecimal("12.00"))
                .setQuantityInStock(8)
                .setMinimumStockLevel(3)
                .setLocation("I9-J10")
                .setSupplier("WiperPro Inc.")
                .setCreatedAt(java.time.Instant.now())
                .setUpdatedAt(java.time.Instant.now());
                
        mockParts.add(part4);
        mockParts.add(part5);
        
        return mockParts;
    }
    
    /**
     * Save a spare part
     * @param sparePart The spare part to save
     * @return The saved spare part
     */
    public SparePart save(SparePart sparePart) {
        return sparePartRepository.save(sparePart);
    }
    
    /**
     * Delete a spare part by its ID
     * @param id The spare part ID
     */
    public void deleteById(Integer id) {
        sparePartRepository.deleteById(id);
    }
    
    /**
     * Find spare parts by category
     * @param category The category to search for
     * @return List of spare parts in the specified category
     */
    public List<SparePart> findByCategory(String category) {
        return sparePartRepository.findAll().stream()
                .filter(part -> category.equals(part.getCategory()))
                .toList();
    }
    
    /**
     * Update the stock quantity of a spare part
     * @param id The spare part ID
     * @param quantity The new quantity
     * @return The updated spare part
     */
    public Optional<SparePart> updateStockQuantity(Integer id, Integer quantity) {
        return sparePartRepository.findById(id)
                .map(part -> {
                    part.setQuantityInStock(quantity);
                    return sparePartRepository.save(part);
                });
    }
} 
package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.utc.service.entity.SparePart;
import vn.utc.service.repo.SparePartRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SparePartService {
    
    private final SparePartRepository sparePartRepository;
    
    /**
     * Find all spare parts that are below their minimum stock level
     * @return List of spare parts with low stock
     */
    public List<SparePart> findLowStockItems() {
        return sparePartRepository.findAll().stream()
                .filter(part -> part.getQuantityInStock() <= part.getMinimumStockLevel())
                .toList();
    }
    
    /**
     * Find all spare parts that are below their minimum stock level with pagination
     * @param pageable Pagination parameters
     * @return Page of spare parts with low stock
     */
    public Page<SparePart> findLowStockItems(Pageable pageable) {
        List<SparePart> allParts = sparePartRepository.findAll();
        List<SparePart> lowStockItems = allParts.stream()
                .filter(part -> part.getQuantityInStock() <= part.getMinimumStockLevel())
                .toList();
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), lowStockItems.size());
        
        if (start > lowStockItems.size()) {
            return new PageImpl<>(List.of(), pageable, lowStockItems.size());
        }
        
        return new PageImpl<>(lowStockItems.subList(start, end), pageable, lowStockItems.size());
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
        return sparePartRepository.findAll();
    }
    
    /**
     * Find all spare parts with pagination
     * @param pageable Pagination parameters
     * @return Page of all spare parts
     */
    public Page<SparePart> findAll(Pageable pageable) {
        return sparePartRepository.findAll(pageable);
    }
    
    /**
     * Find all spare parts with filters and pagination
     * @param pageable Pagination parameters
     * @param category Category filter
     * @param search Search term filter
     * @param stockStatus Stock status filter
     * @return Page of filtered spare parts
     */
    public Page<SparePart> findAll(Pageable pageable, String category, String search, String stockStatus) {
        List<SparePart> allParts = sparePartRepository.findAll();
        
        // Apply filters
        List<SparePart> filteredParts = allParts.stream()
                .filter(part -> category == null || category.isEmpty() || category.equals(part.getCategory()))
                .filter(part -> search == null || search.isEmpty() || 
                        part.getName().toLowerCase().contains(search.toLowerCase()) ||
                        part.getDescription().toLowerCase().contains(search.toLowerCase()))
                .filter(part -> stockStatus == null || stockStatus.isEmpty() || 
                        (stockStatus.equals("LOW") && part.getQuantityInStock() <= part.getMinimumStockLevel()) ||
                        (stockStatus.equals("MODERATE") && part.getQuantityInStock() > part.getMinimumStockLevel() && part.getQuantityInStock() <= part.getMinimumStockLevel() * 2) ||
                        (stockStatus.equals("ADEQUATE") && part.getQuantityInStock() > part.getMinimumStockLevel() * 2))
                .toList();
        
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredParts.size());
        
        if (start > filteredParts.size()) {
            return new PageImpl<>(List.of(), pageable, filteredParts.size());
        }
        
        return new PageImpl<>(filteredParts.subList(start, end), pageable, filteredParts.size());
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
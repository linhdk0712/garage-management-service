package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import vn.utc.service.entity.SparePart;
import vn.utc.service.repo.SparePartRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SparePartService Unit Tests")
class SparePartServiceTest {

    @Mock
    private SparePartRepository sparePartRepository;

    @InjectMocks
    private SparePartService sparePartService;

    private SparePart sparePart;
    private SparePart lowStockPart;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        
        sparePart = new SparePart()
                .setId(1)
                .setName("Oil Filter")
                .setDescription("High-quality oil filter for engine protection")
                .setCategory("Filters")
                .setPrice(new BigDecimal("15.99"))
                .setCost(new BigDecimal("8.50"))
                .setQuantityInStock(10)
                .setMinimumStockLevel(5)
                .setLocation("A1-B2")
                .setSupplier("AutoParts Co.")
                .setCreatedAt(now)
                .setUpdatedAt(now);

        lowStockPart = new SparePart()
                .setId(2)
                .setName("Brake Pads")
                .setDescription("Ceramic brake pads for optimal stopping power")
                .setCategory("Brakes")
                .setPrice(new BigDecimal("45.99"))
                .setCost(new BigDecimal("25.00"))
                .setQuantityInStock(2)
                .setMinimumStockLevel(5)
                .setLocation("C3-D4")
                .setSupplier("BrakeTech Inc.")
                .setCreatedAt(now)
                .setUpdatedAt(now);
    }

    @Test
    @DisplayName("Should find low stock items when parts exist")
    void findLowStockItems_WhenPartsExist_ShouldReturnLowStockItems() {
        // Given
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        List<SparePart> result = sparePartService.findLowStockItems();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("Brake Pads");
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no parts exist")
    void findLowStockItems_WhenNoPartsExist_ShouldReturnEmptyList() {
        // Given
        when(sparePartRepository.findAll()).thenReturn(List.of());

        // When
        List<SparePart> result = sparePartService.findLowStockItems();

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no low stock items")
    void findLowStockItems_WhenNoLowStockItems_ShouldReturnEmptyList() {
        // Given
        SparePart wellStockedPart = new SparePart()
                .setId(3)
                .setQuantityInStock(20)
                .setMinimumStockLevel(5);
        
        when(sparePartRepository.findAll()).thenReturn(List.of(wellStockedPart));

        // When
        List<SparePart> result = sparePartService.findLowStockItems();

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should find low stock items with pagination when parts exist")
    void findLowStockItems_WithPageable_WhenPartsExist_ShouldReturnPaginatedLowStockItems() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        Page<SparePart> result = sparePartService.findLowStockItems(pageable);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(2);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty page when no parts exist")
    void findLowStockItems_WithPageable_WhenNoPartsExist_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        when(sparePartRepository.findAll()).thenReturn(List.of());

        // When
        Page<SparePart> result = sparePartService.findLowStockItems(pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should find spare part by ID when exists")
    void findById_WhenPartExists_ShouldReturnSparePart() {
        // Given
        when(sparePartRepository.findById(1)).thenReturn(Optional.of(sparePart));

        // When
        Optional<SparePart> result = sparePartService.findById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
        assertThat(result.get().getName()).isEqualTo("Oil Filter");
        verify(sparePartRepository).findById(1);
    }

    @Test
    @DisplayName("Should return empty when spare part not found by ID")
    void findById_WhenPartNotFound_ShouldReturnEmpty() {
        // Given
        when(sparePartRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<SparePart> result = sparePartService.findById(999);

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findById(999);
    }

    @Test
    @DisplayName("Should return all spare parts when parts exist")
    void findAll_WhenPartsExist_ShouldReturnAllParts() {
        // Given
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        List<SparePart> result = sparePartService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).isEqualTo(allParts);
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no parts exist")
    void findAll_WhenNoPartsExist_ShouldReturnEmptyList() {
        // Given
        when(sparePartRepository.findAll()).thenReturn(List.of());

        // When
        List<SparePart> result = sparePartService.findAll();

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return paginated spare parts when parts exist")
    void findAll_WithPageable_WhenPartsExist_ShouldReturnPaginatedParts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<SparePart> expectedPage = new PageImpl<>(List.of(sparePart, lowStockPart), pageable, 2);
        when(sparePartRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(sparePartRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty page when no parts exist")
    void findAll_WithPageable_WhenNoPartsExist_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<SparePart> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(sparePartRepository.findAll(pageable)).thenReturn(emptyPage);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(sparePartRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return filtered spare parts when filters provided")
    void findAll_WithFilters_WhenPartsExist_ShouldReturnFilteredParts() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String category = "Filters";
        String search = "Oil";
        String stockStatus = "MODERATE";
        
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable, category, search, stockStatus);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Oil Filter");
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty page with filters when no parts exist")
    void findAll_WithFilters_WhenNoPartsExist_ShouldReturnEmptyPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String category = "Filters";
        String search = "Oil";
        String stockStatus = "LOW";
        
        when(sparePartRepository.findAll()).thenReturn(List.of());

        // When
        Page<SparePart> result = sparePartService.findAll(pageable, category, search, stockStatus);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should filter by category correctly")
    void findAll_WithCategoryFilter_ShouldFilterByCategory() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String category = "Filters";
        
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable, category, null, null);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCategory()).isEqualTo("Filters");
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should filter by search term correctly")
    void findAll_WithSearchFilter_ShouldFilterBySearchTerm() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "Brake";
        
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable, null, search, null);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Brake Pads");
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should filter by stock status correctly")
    void findAll_WithStockStatusFilter_ShouldFilterByStockStatus() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String stockStatus = "LOW";
        
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        Page<SparePart> result = sparePartService.findAll(pageable, null, null, stockStatus);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(2); // lowStockPart
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should save spare part successfully")
    void save_ShouldSaveAndReturnSparePart() {
        // Given
        SparePart newPart = new SparePart()
                .setName("New Part")
                .setDescription("New part description")
                .setCategory("New Category")
                .setPrice(new BigDecimal("25.99"))
                .setCost(new BigDecimal("15.00"))
                .setQuantityInStock(5)
                .setMinimumStockLevel(3);

        SparePart savedPart = new SparePart()
                .setId(3)
                .setName("New Part")
                .setDescription("New part description")
                .setCategory("New Category")
                .setPrice(new BigDecimal("25.99"))
                .setCost(new BigDecimal("15.00"))
                .setQuantityInStock(5)
                .setMinimumStockLevel(3)
                .setCreatedAt(now)
                .setUpdatedAt(now);

        when(sparePartRepository.save(newPart)).thenReturn(savedPart);

        // When
        SparePart result = sparePartService.save(newPart);

        // Then
        assertThat(result).isEqualTo(savedPart);
        assertThat(result.getId()).isEqualTo(3);
        verify(sparePartRepository).save(newPart);
    }

    @Test
    @DisplayName("Should delete spare part by ID")
    void deleteById_ShouldDeleteSparePart() {
        // Given
        doNothing().when(sparePartRepository).deleteById(1);

        // When
        sparePartService.deleteById(1);

        // Then
        verify(sparePartRepository).deleteById(1);
    }

    @Test
    @DisplayName("Should find spare parts by category")
    void findByCategory_ShouldReturnPartsInCategory() {
        // Given
        String category = "Filters";
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        List<SparePart> result = sparePartService.findByCategory(category);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("Filters");
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no parts in category")
    void findByCategory_WhenNoPartsInCategory_ShouldReturnEmptyList() {
        // Given
        String category = "NonExistentCategory";
        List<SparePart> allParts = List.of(sparePart, lowStockPart);
        when(sparePartRepository.findAll()).thenReturn(allParts);

        // When
        List<SparePart> result = sparePartService.findByCategory(category);

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findAll();
    }

    @Test
    @DisplayName("Should update stock quantity successfully")
    void updateStockQuantity_WhenPartExists_ShouldUpdateQuantity() {
        // Given
        Integer newQuantity = 15;
        SparePart updatedPart = new SparePart()
                .setId(1)
                .setName("Oil Filter")
                .setQuantityInStock(newQuantity)
                .setMinimumStockLevel(5);

        when(sparePartRepository.findById(1)).thenReturn(Optional.of(sparePart));
        when(sparePartRepository.save(any(SparePart.class))).thenReturn(updatedPart);

        // When
        Optional<SparePart> result = sparePartService.updateStockQuantity(1, newQuantity);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getQuantityInStock()).isEqualTo(newQuantity);
        verify(sparePartRepository).findById(1);
        verify(sparePartRepository).save(any(SparePart.class));
    }

    @Test
    @DisplayName("Should return empty when part not found for stock update")
    void updateStockQuantity_WhenPartNotFound_ShouldReturnEmpty() {
        // Given
        when(sparePartRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<SparePart> result = sparePartService.updateStockQuantity(999, 10);

        // Then
        assertThat(result).isEmpty();
        verify(sparePartRepository).findById(999);
        verify(sparePartRepository, never()).save(any(SparePart.class));
    }
} 
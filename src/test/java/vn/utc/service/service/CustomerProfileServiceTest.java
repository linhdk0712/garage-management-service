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
import vn.utc.service.dtos.CustomerProfileDto;
import vn.utc.service.entity.CustomerProfile;
import vn.utc.service.mapper.CustomerProfileMapper;
import vn.utc.service.repo.CustomerProfileRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerProfileService Unit Tests")
class CustomerProfileServiceTest {

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private CustomerProfileMapper customerProfileMapper;

    @InjectMocks
    private CustomerProfileService customerProfileService;

    private CustomerProfile customerProfile;
    private CustomerProfileDto customerProfileDto;

    @BeforeEach
    void setUp() {
        customerProfile = new CustomerProfile();
        customerProfile.setUserId(1);
        customerProfile.setUsername("testuser");
        customerProfile.setFirstName("John");
        customerProfile.setLastName("Doe");
        customerProfile.setEmail("john.doe@example.com");
        customerProfile.setPhone("1234567890");
        customerProfile.setAddress("123 Test St");
        customerProfile.setCity("Test City");
        customerProfile.setState("Test State");
        customerProfile.setZipCode("12345");
        customerProfile.setCreatedAt(Instant.now());
        customerProfile.setLastLogin(Instant.now());
        customerProfile.setIsActive(true);
        customerProfile.setNotes("Test notes");

        customerProfileDto = new CustomerProfileDto(
                1,
                "testuser",
                "john.doe@example.com",
                "1234567890",
                Instant.now(),
                Instant.now(),
                true,
                "John",
                "Doe",
                "123 Test St",
                "Test City",
                "Test State",
                "12345",
                "Test notes"
        );
    }

    @Test
    @DisplayName("Should return all customer profiles")
    void findAllCustomerProfiles_ShouldReturnAllProfiles() {
        // Given
        List<CustomerProfile> profiles = List.of(customerProfile);
        List<CustomerProfileDto> expectedDtos = List.of(customerProfileDto);

        when(customerProfileRepository.findAll()).thenReturn(profiles);
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        List<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDtos);
        verify(customerProfileRepository).findAll();
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should return empty list when no customer profiles exist")
    void findAllCustomerProfiles_WhenNoProfiles_ShouldReturnEmptyList() {
        // Given
        when(customerProfileRepository.findAll()).thenReturn(List.of());

        // When
        List<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles();

        // Then
        assertThat(result).isEmpty();
        verify(customerProfileRepository).findAll();
        verifyNoInteractions(customerProfileMapper);
    }

    @Test
    @DisplayName("Should return paginated customer profiles")
    void findAllCustomerProfiles_WithPageable_ShouldReturnPaginatedProfiles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerProfile> profilePage = new PageImpl<>(List.of(customerProfile), pageable, 1);
        Page<CustomerProfileDto> expectedPage = new PageImpl<>(List.of(customerProfileDto), pageable, 1);

        when(customerProfileRepository.findAll(pageable)).thenReturn(profilePage);
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        Page<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerProfileRepository).findAll(pageable);
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should return filtered customer profiles when search and status provided")
    void findAllCustomerProfiles_WithSearchAndStatus_ShouldReturnFilteredProfiles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "John";
        String status = "ACTIVE";
        Page<CustomerProfile> profilePage = new PageImpl<>(List.of(customerProfile), pageable, 1);
        Page<CustomerProfileDto> expectedPage = new PageImpl<>(List.of(customerProfileDto), pageable, 1);

        when(customerProfileRepository.findBySearchAndStatus(search, status, pageable))
                .thenReturn(profilePage);
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        Page<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles(pageable, search, status);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerProfileRepository).findBySearchAndStatus(search, status, pageable);
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should return all customer profiles when search and status are empty")
    void findAllCustomerProfiles_WithEmptySearchAndStatus_ShouldReturnAllProfiles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "";
        String status = "";
        Page<CustomerProfile> profilePage = new PageImpl<>(List.of(customerProfile), pageable, 1);
        Page<CustomerProfileDto> expectedPage = new PageImpl<>(List.of(customerProfileDto), pageable, 1);

        when(customerProfileRepository.findAll(pageable)).thenReturn(profilePage);
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        Page<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles(pageable, search, status);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerProfileRepository).findAll(pageable);
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should return all customer profiles when search and status are null")
    void findAllCustomerProfiles_WithNullSearchAndStatus_ShouldReturnAllProfiles() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerProfile> profilePage = new PageImpl<>(List.of(customerProfile), pageable, 1);
        Page<CustomerProfileDto> expectedPage = new PageImpl<>(List.of(customerProfileDto), pageable, 1);

        when(customerProfileRepository.findAll(pageable)).thenReturn(profilePage);
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        Page<CustomerProfileDto> result = customerProfileService.findAllCustomerProfiles(pageable, null, null);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerProfileRepository).findAll(pageable);
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should find customer profile by username when exists")
    void findCustomerProfileByUsername_WhenProfileExists_ShouldReturnProfile() {
        // Given
        when(customerProfileRepository.findCustomerProfileByUsername("testuser"))
                .thenReturn(Optional.of(customerProfile));
        when(customerProfileMapper.toDto(customerProfile)).thenReturn(customerProfileDto);

        // When
        Optional<CustomerProfileDto> result = customerProfileService.findCustomerProfileByUsername("testuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(customerProfileDto);
        verify(customerProfileRepository).findCustomerProfileByUsername("testuser");
        verify(customerProfileMapper).toDto(customerProfile);
    }

    @Test
    @DisplayName("Should return empty when customer profile not found by username")
    void findCustomerProfileByUsername_WhenProfileNotFound_ShouldReturnEmpty() {
        // Given
        when(customerProfileRepository.findCustomerProfileByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        // When
        Optional<CustomerProfileDto> result = customerProfileService.findCustomerProfileByUsername("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(customerProfileRepository).findCustomerProfileByUsername("nonexistent");
        verifyNoInteractions(customerProfileMapper);
    }

    @Test
    @DisplayName("Should handle case-sensitive username search")
    void findCustomerProfileByUsername_WithCaseSensitiveUsername_ShouldFindExactMatch() {
        // Given
        when(customerProfileRepository.findCustomerProfileByUsername("TestUser"))
                .thenReturn(Optional.empty());

        // When
        Optional<CustomerProfileDto> result = customerProfileService.findCustomerProfileByUsername("TestUser");

        // Then
        assertThat(result).isEmpty();
        verify(customerProfileRepository).findCustomerProfileByUsername("TestUser");
        verifyNoInteractions(customerProfileMapper);
    }

    @Test
    @DisplayName("Should handle empty username")
    void findCustomerProfileByUsername_WithEmptyUsername_ShouldReturnEmpty() {
        // Given
        when(customerProfileRepository.findCustomerProfileByUsername(""))
                .thenReturn(Optional.empty());

        // When
        Optional<CustomerProfileDto> result = customerProfileService.findCustomerProfileByUsername("");

        // Then
        assertThat(result).isEmpty();
        verify(customerProfileRepository).findCustomerProfileByUsername("");
        verifyNoInteractions(customerProfileMapper);
    }

    @Test
    @DisplayName("Should handle null username")
    void findCustomerProfileByUsername_WithNullUsername_ShouldReturnEmpty() {
        // Given
        when(customerProfileRepository.findCustomerProfileByUsername(null))
                .thenReturn(Optional.empty());

        // When
        Optional<CustomerProfileDto> result = customerProfileService.findCustomerProfileByUsername(null);

        // Then
        assertThat(result).isEmpty();
        verify(customerProfileRepository).findCustomerProfileByUsername(null);
        verifyNoInteractions(customerProfileMapper);
    }
} 
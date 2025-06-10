package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.utc.service.dtos.StaffProfileDto;
import vn.utc.service.entity.StaffProfile;
import vn.utc.service.mapper.StaffProfileMapper;
import vn.utc.service.repo.StaffProfileRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StaffProfileService Unit Tests")
class StaffProfileServiceTest {

    @Mock
    private StaffProfileRepository staffProfileRepository;

    @Mock
    private StaffProfileMapper staffProfileMapper;

    @InjectMocks
    private StaffProfileService staffProfileService;

    private StaffProfile staffProfile;
    private StaffProfileDto staffProfileDto;

    @BeforeEach
    void setUp() {
        staffProfile = new StaffProfile();
        staffProfile.setUserId(1);
        staffProfile.setUsername("staffuser");
        staffProfile.setFirstName("John");
        staffProfile.setLastName("Doe");
        staffProfile.setEmail("john.doe@example.com");
        staffProfile.setPhone("1234567890");
        staffProfile.setPosition("Mechanic");
        staffProfile.setSpecialization("Engine Repair");
        staffProfile.setHourlyRate(new BigDecimal("25.00"));
        staffProfile.setHireDate(LocalDate.of(2023, 1, 1));
        staffProfile.setCreatedAt(Instant.now());
        staffProfile.setLastLogin(Instant.now());
        staffProfile.setIsActive(true);

        staffProfileDto = new StaffProfileDto(
                1,
                "staffuser",
                "john.doe@example.com",
                "1234567890",
                Instant.now(),
                Instant.now(),
                true,
                "John",
                "Doe",
                LocalDate.of(2023, 1, 1),
                new BigDecimal("25.00"),
                "Mechanic",
                "Engine Repair"
        );
    }

    @Test
    @DisplayName("Should find staff profile by username when exists")
    void findStaffProfileByUsername_WhenProfileExists_ShouldReturnProfile() {
        // Given
        when(staffProfileRepository.findByUsername("staffuser"))
                .thenReturn(Optional.of(staffProfile));
        when(staffProfileMapper.toDto(staffProfile)).thenReturn(staffProfileDto);

        // When
        Optional<StaffProfileDto> result = staffProfileService.findStaffProfileByUsername("staffuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(staffProfileDto);
        verify(staffProfileRepository).findByUsername("staffuser");
        verify(staffProfileMapper).toDto(staffProfile);
    }

    @Test
    @DisplayName("Should return empty when staff profile not found by username")
    void findStaffProfileByUsername_WhenProfileNotFound_ShouldReturnEmpty() {
        // Given
        when(staffProfileRepository.findByUsername("nonexistent"))
                .thenReturn(Optional.empty());

        // When
        Optional<StaffProfileDto> result = staffProfileService.findStaffProfileByUsername("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(staffProfileRepository).findByUsername("nonexistent");
        verifyNoInteractions(staffProfileMapper);
    }

    @Test
    @DisplayName("Should handle case-sensitive username search")
    void findStaffProfileByUsername_WithCaseSensitiveUsername_ShouldFindExactMatch() {
        // Given
        when(staffProfileRepository.findByUsername("StaffUser"))
                .thenReturn(Optional.empty());

        // When
        Optional<StaffProfileDto> result = staffProfileService.findStaffProfileByUsername("StaffUser");

        // Then
        assertThat(result).isEmpty();
        verify(staffProfileRepository).findByUsername("StaffUser");
        verifyNoInteractions(staffProfileMapper);
    }

    @Test
    @DisplayName("Should handle empty username")
    void findStaffProfileByUsername_WithEmptyUsername_ShouldReturnEmpty() {
        // Given
        when(staffProfileRepository.findByUsername(""))
                .thenReturn(Optional.empty());

        // When
        Optional<StaffProfileDto> result = staffProfileService.findStaffProfileByUsername("");

        // Then
        assertThat(result).isEmpty();
        verify(staffProfileRepository).findByUsername("");
        verifyNoInteractions(staffProfileMapper);
    }

    @Test
    @DisplayName("Should handle null username")
    void findStaffProfileByUsername_WithNullUsername_ShouldReturnEmpty() {
        // Given
        when(staffProfileRepository.findByUsername(null))
                .thenReturn(Optional.empty());

        // When
        Optional<StaffProfileDto> result = staffProfileService.findStaffProfileByUsername(null);

        // Then
        assertThat(result).isEmpty();
        verify(staffProfileRepository).findByUsername(null);
        verifyNoInteractions(staffProfileMapper);
    }
} 
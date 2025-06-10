package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.utc.service.dtos.RoleDto;
import vn.utc.service.entity.Role;
import vn.utc.service.mapper.RoleMapper;
import vn.utc.service.repo.RoleRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleService Unit Tests")
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleDto roleDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("STAFF");
        role.setDescription("Staff role for garage employees");
        role.setCreatedAt(Instant.now());
        role.setUpdatedAt(Instant.now());

        roleDto = new RoleDto(1, "STAFF", "Staff role for garage employees", Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("Should find role by name when role exists")
    void findByName_WhenRoleExists_ShouldReturnRoleDto() {
        // Given
        when(roleRepository.findByName("STAFF")).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDto);

        // When
        Optional<RoleDto> result = roleService.findByName("STAFF");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(roleDto);
        verify(roleRepository).findByName("STAFF");
        verify(roleMapper).toDto(role);
    }

    @Test
    @DisplayName("Should return empty when role not found by name")
    void findByName_WhenRoleNotFound_ShouldReturnEmpty() {
        // Given
        when(roleRepository.findByName("NONEXISTENT")).thenReturn(Optional.empty());

        // When
        Optional<RoleDto> result = roleService.findByName("NONEXISTENT");

        // Then
        assertThat(result).isEmpty();
        verify(roleRepository).findByName("NONEXISTENT");
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Should return empty when role is null")
    void findByName_WhenRoleIsNull_ShouldReturnEmpty() {
        // Given
        when(roleRepository.findByName("NULL_ROLE")).thenReturn(Optional.empty());

        // When
        Optional<RoleDto> result = roleService.findByName("NULL_ROLE");

        // Then
        assertThat(result).isEmpty();
        verify(roleRepository).findByName("NULL_ROLE");
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Should handle case-sensitive role names")
    void findByName_WithCaseSensitiveName_ShouldFindExactMatch() {
        // Given
        when(roleRepository.findByName("staff")).thenReturn(Optional.empty());

        // When
        Optional<RoleDto> result = roleService.findByName("staff");

        // Then
        assertThat(result).isEmpty();
        verify(roleRepository).findByName("staff");
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Should handle empty role name")
    void findByName_WithEmptyName_ShouldReturnEmpty() {
        // Given
        when(roleRepository.findByName("")).thenReturn(Optional.empty());

        // When
        Optional<RoleDto> result = roleService.findByName("");

        // Then
        assertThat(result).isEmpty();
        verify(roleRepository).findByName("");
        verifyNoInteractions(roleMapper);
    }

    @Test
    @DisplayName("Should handle null role name")
    void findByName_WithNullName_ShouldReturnEmpty() {
        // Given
        when(roleRepository.findByName(null)).thenReturn(Optional.empty());

        // When
        Optional<RoleDto> result = roleService.findByName(null);

        // Then
        assertThat(result).isEmpty();
        verify(roleRepository).findByName(null);
        verifyNoInteractions(roleMapper);
    }
} 
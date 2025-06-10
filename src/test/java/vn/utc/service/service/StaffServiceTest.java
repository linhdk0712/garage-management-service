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
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.dtos.RoleDto;
import vn.utc.service.dtos.StaffDto;
import vn.utc.service.dtos.StaffRequest;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.Role;
import vn.utc.service.entity.Staff;
import vn.utc.service.entity.User;
import vn.utc.service.mapper.StaffMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.StaffRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StaffService Unit Tests")
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private UserService userService;

    @Mock
    private StaffMapper staffMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StaffService staffService;

    private Staff staff;
    private StaffDto staffDto;
    private User user;
    private UserDto userDto;
    private Role role;
    private RoleDto roleDto;
    private StaffRequest staffRequest;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1);
        role.setName("STAFF");

        roleDto = new RoleDto(1, "STAFF", null, null, null);

        user = new User();
        user.setId(1);
        user.setUsername("staffuser");
        user.setEmail("staff@example.com");
        user.setPhone("1234567890");
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("staffuser");
        userDto.setEmail("staff@example.com");
        userDto.setPhone("1234567890");
        userDto.setRole("STAFF");
        userDto.setActive(true);
        userDto.setCreatedAt(Instant.now());
        Set<RoleDto> roles = new HashSet<>();
        roles.add(roleDto);
        userDto.setRoles(roles);

        staff = new Staff();
        staff.setId(1);
        staff.setUser(user);
        staff.setFirstName("John");
        staff.setLastName("Doe");
        staff.setHourlyRate(new BigDecimal("25.00"));
        staff.setHireDate(LocalDate.of(2023, 1, 1));
        staff.setPosition("Mechanic");
        staff.setSpecialization("Engine Repair");

        staffDto = new StaffDto(1, "John", "Doe", "Mechanic", "Engine Repair", LocalDate.of(2023, 1, 1), new BigDecimal("25.00"));

        staffRequest = new StaffRequest(
                "newstaff",
                "newstaff@example.com",
                "0987654321",
                "password123",
                "Jane",
                "Smith",
                "456 Staff St",
                "Staff City",
                "Staff State",
                "12345",
                "Phone",
                "Technician",
                "Electrical Systems",
                LocalDate.of(2024, 1, 1),
                new BigDecimal("30.00")
        );
    }

    @Test
    @DisplayName("Should find staff by username when staff exists")
    void findByUser_WhenStaffExists_ShouldReturnStaffDto() {
        // Given
        when(userService.findByUsername("staffuser")).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(staffRepository.findStaffByUser(user)).thenReturn(Optional.of(staff));
        when(staffMapper.toDto(staff)).thenReturn(staffDto);

        // When
        Optional<StaffDto> result = staffService.findByUser("staffuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(staffDto);
        verify(userService).findByUsername("staffuser");
        verify(userMapper).toEntity(userDto);
        verify(staffRepository).findStaffByUser(user);
        verify(staffMapper).toDto(staff);
    }

    @Test
    @DisplayName("Should return empty when user not found")
    void findByUser_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<StaffDto> result = staffService.findByUser("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(userService).findByUsername("nonexistent");
        verifyNoInteractions(userMapper, staffRepository, staffMapper);
    }

    @Test
    @DisplayName("Should return empty when staff not found for user")
    void findByUser_WhenStaffNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findByUsername("staffuser")).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(staffRepository.findStaffByUser(user)).thenReturn(Optional.empty());

        // When
        Optional<StaffDto> result = staffService.findByUser("staffuser");

        // Then
        assertThat(result).isEmpty();
        verify(userService).findByUsername("staffuser");
        verify(userMapper).toEntity(userDto);
        verify(staffRepository).findStaffByUser(user);
        verifyNoInteractions(staffMapper);
    }

    @Test
    @DisplayName("Should return all staff")
    void findAll_ShouldReturnAllStaff() {
        // Given
        List<Staff> staffList = List.of(staff);
        List<StaffDto> expectedDtos = List.of(staffDto);

        when(staffRepository.findAll()).thenReturn(staffList);
        when(staffMapper.toDto(staff)).thenReturn(staffDto);

        // When
        Optional<List<StaffDto>> result = staffService.findAll();

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        assertThat(result.get()).isEqualTo(expectedDtos);
        verify(staffRepository).findAll();
        verify(staffMapper).toDto(staff);
    }

    @Test
    @DisplayName("Should return empty list when no staff exist")
    void findAll_WhenNoStaff_ShouldReturnEmptyList() {
        // Given
        when(staffRepository.findAll()).thenReturn(List.of());

        // When
        Optional<List<StaffDto>> result = staffService.findAll();

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEmpty();
        verify(staffRepository).findAll();
        verifyNoInteractions(staffMapper);
    }

    @Test
    @DisplayName("Should return paginated staff")
    void findAll_WithPageable_ShouldReturnPaginatedStaff() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Staff> staffPage = new PageImpl<>(List.of(staff), pageable, 1);
        Page<StaffDto> expectedPage = new PageImpl<>(List.of(staffDto), pageable, 1);

        when(staffRepository.findAll(pageable)).thenReturn(staffPage);
        when(staffMapper.toDto(staff)).thenReturn(staffDto);

        // When
        Page<StaffDto> result = staffService.findAll(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(staffRepository).findAll(pageable);
        verify(staffMapper).toDto(staff);
    }

    @Test
    @DisplayName("Should create staff successfully")
    void createStaff_ShouldCreateAndReturnStaffDto() {
        // Given
        UserDto newUserDto = new UserDto();
        newUserDto.setId(2);
        newUserDto.setUsername("newstaff");
        newUserDto.setEmail("newstaff@example.com");
        newUserDto.setPhone("0987654321");
        newUserDto.setRole("STAFF");
        newUserDto.setActive(true);
        newUserDto.setCreatedAt(Instant.now());
        Set<RoleDto> roles = new HashSet<>();
        roles.add(roleDto);
        newUserDto.setRoles(roles);

        User newUser = new User();
        newUser.setId(2);
        newUser.setUsername("newstaff");
        newUser.setEmail("newstaff@example.com");

        Staff newStaff = new Staff();
        newStaff.setId(2);
        newStaff.setUser(newUser);
        newStaff.setFirstName("Jane");
        newStaff.setLastName("Smith");
        newStaff.setHourlyRate(new BigDecimal("30.00"));
        newStaff.setHireDate(LocalDate.of(2024, 1, 1));
        newStaff.setPosition("Technician");
        newStaff.setSpecialization("Electrical Systems");

        StaffDto newStaffDto = new StaffDto(2, "Jane", "Smith", "Technician", "Electrical Systems", LocalDate.of(2024, 1, 1), new BigDecimal("30.00"));

        when(roleService.findByName(ContsConfig.STAFF)).thenReturn(Optional.of(roleDto));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.save(any(UserDto.class))).thenReturn(newUserDto);
        when(userMapper.toEntity(newUserDto)).thenReturn(newUser);
        when(staffRepository.save(any(Staff.class))).thenReturn(newStaff);
        when(staffMapper.toDto(newStaff)).thenReturn(newStaffDto);

        // When
        Optional<StaffDto> result = staffService.createStaff(staffRequest);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(newStaffDto);
        verify(roleService).findByName(ContsConfig.STAFF);
        verify(passwordEncoder).encode("password123");
        verify(userService).save(any(UserDto.class));
        verify(userMapper).toEntity(newUserDto);
        verify(staffRepository).save(any(Staff.class));
        verify(staffMapper).toDto(newStaff);
    }

    @Test
    @DisplayName("Should throw exception when role not found")
    void createStaff_WhenRoleNotFound_ShouldThrowException() {
        // Given
        when(roleService.findByName(ContsConfig.STAFF)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> staffService.createStaff(staffRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Error: Role is not found.");
        verify(roleService).findByName(ContsConfig.STAFF);
        verifyNoInteractions(passwordEncoder, userService, userMapper, staffRepository, staffMapper);
    }

    @Test
    @DisplayName("Should create user with correct data when creating staff")
    void createStaff_ShouldCreateUserWithCorrectData() {
        // Given
        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(2);
        savedUserDto.setUsername("newstaff");
        savedUserDto.setEmail("newstaff@example.com");

        Staff savedStaff = new Staff();
        savedStaff.setId(2);

        StaffDto savedStaffDto = new StaffDto(2, "Jane", "Smith", "Technician", "Electrical Systems", LocalDate.of(2024, 1, 1), new BigDecimal("30.00"));

        when(roleService.findByName(ContsConfig.STAFF)).thenReturn(Optional.of(roleDto));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.save(any(UserDto.class))).thenAnswer(invocation -> {
            UserDto userDtoArg = invocation.getArgument(0);
            assertThat(userDtoArg.getUsername()).isEqualTo("newstaff");
            assertThat(userDtoArg.getEmail()).isEqualTo("newstaff@example.com");
            assertThat(userDtoArg.getPhone()).isEqualTo("0987654321");
            assertThat(userDtoArg.getPassword()).isEqualTo("encodedPassword");
            assertThat(userDtoArg.getRole()).isEqualTo("STAFF");
            assertThat(userDtoArg.getActive()).isTrue();
            return savedUserDto;
        });
        when(userMapper.toEntity(savedUserDto)).thenReturn(new User());
        when(staffRepository.save(any(Staff.class))).thenReturn(savedStaff);
        when(staffMapper.toDto(savedStaff)).thenReturn(savedStaffDto);

        // When
        Optional<StaffDto> result = staffService.createStaff(staffRequest);

        // Then
        assertThat(result).isPresent();
        verify(userService).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Should create staff with correct data")
    void createStaff_ShouldCreateStaffWithCorrectData() {
        // Given
        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(2);

        User newUser = new User();
        newUser.setId(2);

        Staff savedStaff = new Staff();
        savedStaff.setId(2);

        StaffDto savedStaffDto = new StaffDto(2, "Jane", "Smith", "Technician", "Electrical Systems", LocalDate.of(2024, 1, 1), new BigDecimal("30.00"));

        when(roleService.findByName(ContsConfig.STAFF)).thenReturn(Optional.of(roleDto));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userService.save(any(UserDto.class))).thenReturn(savedUserDto);
        when(userMapper.toEntity(savedUserDto)).thenReturn(newUser);
        when(staffRepository.save(any(Staff.class))).thenAnswer(invocation -> {
            Staff staffArg = invocation.getArgument(0);
            assertThat(staffArg.getFirstName()).isEqualTo("Jane");
            assertThat(staffArg.getLastName()).isEqualTo("Smith");
            assertThat(staffArg.getPosition()).isEqualTo("Technician");
            assertThat(staffArg.getSpecialization()).isEqualTo("Electrical Systems");
            assertThat(staffArg.getHourlyRate()).isEqualTo(new BigDecimal("30.00"));
            assertThat(staffArg.getHireDate()).isEqualTo(LocalDate.of(2024, 1, 1));
            return savedStaff;
        });
        when(staffMapper.toDto(savedStaff)).thenReturn(savedStaffDto);

        // When
        Optional<StaffDto> result = staffService.createStaff(staffRequest);

        // Then
        assertThat(result).isPresent();
        verify(staffRepository).save(any(Staff.class));
    }
} 
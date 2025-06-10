package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.utc.service.config.ContsConfig;
import vn.utc.service.dtos.RoleDto;
import vn.utc.service.dtos.StaffDto;
import vn.utc.service.dtos.StaffRequest;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.Staff;
import vn.utc.service.mapper.StaffMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.StaffRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final UserService userService;
    private  final StaffMapper staffMapper;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Optional<StaffDto> findByUser(String username) {
        UserDto userDto = userService.findByUsername(username).orElse(null);
        if (userDto == null) {
            return Optional.empty();
        }
        return staffRepository.findStaffByUser(userMapper.toEntity(userDto))
                .map(staffMapper::toDto);
    }
    public Optional<List<StaffDto>> findAll() {
        return Optional.of(staffRepository.findAll().stream()
                .map(staffMapper::toDto)
                .toList());
    }
    public Page<StaffDto> findAll(Pageable pageable) {
        return staffRepository.findAll(pageable)
                .map(staffMapper::toDto);
    }
    @Transactional
    public Optional<StaffDto> createStaff(StaffRequest registerRequest) {
        Set<RoleDto> roles = new HashSet<>();
        RoleDto roleDto =
                roleService
                        .findByName(ContsConfig.STAFF)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(roleDto);
        // Create new user's account
        UserDto userDto =
                new UserDto()
                        .setUsername(registerRequest.username())
                        .setEmail(registerRequest.email())
                        .setPassword(passwordEncoder.encode(registerRequest.password()))
                        .setPhone(registerRequest.phone())
                        .setRole(String.valueOf(roles.iterator().next().name()))
                        .setActive(true)
                        .setCreatedAt(Instant.now());
        userDto.setRoles(roles);
        userDto = userService.save(userDto);

        Staff staff = new Staff();
        staff.setUser(userMapper.toEntity(userDto));
        staff.setFirstName(registerRequest.firstName());
        staff.setLastName(registerRequest.lastName());
        staff.setHourlyRate(registerRequest.hourlyRate());
        staff.setHireDate(registerRequest.hireDate());
        staff.setPosition(registerRequest.position());
        staff.setSpecialization(registerRequest.specialization());


        Staff staff1 = staffRepository.save(staff);
        return Optional.of(staffMapper.toDto(staff1));
    }
    @Transactional
    public void createAdminAndManager(StaffRequest registerRequest,String role) {
        Set<RoleDto> roles = new HashSet<>();
        RoleDto roleDto =
                roleService
                        .findByName(role)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(roleDto);
        // Create new user's account
        UserDto userDto =
                new UserDto()
                        .setUsername(registerRequest.username())
                        .setEmail(registerRequest.email())
                        .setPassword(passwordEncoder.encode(registerRequest.password()))
                        .setPhone(registerRequest.phone())
                        .setRole(String.valueOf(roles.iterator().next().name()))
                        .setActive(true)
                        .setCreatedAt(Instant.now());
        userDto.setRoles(roles);
        userDto = userService.save(userDto);

        Staff staff = new Staff();
        staff.setUser(userMapper.toEntity(userDto));
        staff.setFirstName(registerRequest.firstName());
        staff.setLastName(registerRequest.lastName());
        staff.setHourlyRate(registerRequest.hourlyRate());
        staff.setHireDate(registerRequest.hireDate());
        staff.setPosition(registerRequest.position());
        staff.setSpecialization(registerRequest.specialization());


        staffRepository.save(staff);
    }

    /**
     * Save multiple staff members for data initialization purposes
     * @param staffList List of Staff entities to save
     * @return List of saved Staff entities
     */
    @Transactional
    public List<Staff> saveAllForInitialization(List<Staff> staffList) {
        return staffRepository.saveAll(staffList);
    }
}

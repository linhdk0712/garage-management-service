package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.StaffDto;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.mapper.StaffMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.StaffRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;
    private final UserService userService;
    private  final StaffMapper staffMapper;
    private final UserMapper userMapper;

    public Optional<StaffDto> findByUser(String username) {
        UserDto userDto = userService.findByUsername(username).orElse(null);
        if (userDto == null) {
            return Optional.empty();
        }
        return staffRepository.findStaffByUser(userMapper.toEntity(userDto))
                .map(staffMapper::toDto);
    }
}

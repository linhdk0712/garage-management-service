package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.StaffProfileDto;
import vn.utc.service.mapper.StaffProfileMapper;
import vn.utc.service.repo.StaffProfileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StaffProfileService {

    private final StaffProfileRepository staffProfileRepository;
    private final StaffProfileMapper staffProfileMapper;

    public Optional<StaffProfileDto> findStaffProfileByUsername(String userName) {
        return staffProfileRepository.findByUsername(userName)
                .map(staffProfileMapper::toDto);
    }

}

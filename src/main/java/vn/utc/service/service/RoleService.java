package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.RoleDto;
import vn.utc.service.entity.Role;
import vn.utc.service.mapper.RoleMapper;
import vn.utc.service.repo.RoleRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public Optional<RoleDto> findByName(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if(role.isPresent()){
            RoleDto roleDto = roleMapper.toDto(role.get());
            return  Optional.of(roleDto);
        }
        return Optional.empty();
    }
}

package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.dtos.RoleDto;
import vn.utc.service.entity.Role;
import vn.utc.service.mapper.RoleMapper;
import vn.utc.service.repo.RoleRepository;

import java.util.List;
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

    /**
     * Save multiple roles for data initialization purposes
     * @param roles List of Role entities to save
     * @return List of saved Role entities
     */
    @Transactional
    public List<Role> saveAllForInitialization(List<Role> roles) {
        return roleRepository.saveAll(roles);
    }

    /**
     * Find role by name for data initialization purposes
     * @param name The role name to search for
     * @return Optional containing the Role entity if found
     */
    public Optional<Role> findByNameForInitialization(String name) {
        return roleRepository.findByName(name);
    }
}

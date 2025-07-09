package vn.utc.service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.dtos.UserPrincipal;
import vn.utc.service.mapper.UserMapper;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Find user by username or email
    UserDto userDto =
        userService
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User not found with username or email: " + username));

    // Update last login time
    userDto.setLastLogin(Instant.now());
    userService.save(userDto);

    // Create UserPrincipal from the user
    return UserPrincipal.create(userMapper.toEntity(userDto));
  }

  // This method is used by JwtAuthenticationFilter
  @Transactional
  public UserDetails loadUserById(Long id) {
    UserDto user =
        userService
            .findById(Math.toIntExact(id))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

    return UserPrincipal.create(userMapper.toEntity(user));
  }
}

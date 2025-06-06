package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.User;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public Optional<UserDto> findByUsername(String username) {
    Optional<User> user = userRepository.findByUsername(username);
    return user.map(userMapper::toDto);
  }

  public Optional<UserDto> findByEmail(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user.map(userMapper::toDto);
  }

  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }

  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
  @Transactional
  public UserDto save(UserDto userDto) {
    User user = userMapper.toEntity(userDto);
    return userMapper.toDto(userRepository.save(user));
  }

  public Optional<UserDto> findById(int id) {
    User user = userRepository.findById(id).orElse(null);
    return Optional.ofNullable(userMapper.toDto(user));
  }
}

package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.User;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.UserRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhone("1234567890");
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPhone("1234567890");
        userDto.setActive(true);
        userDto.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should find user by username when user exists")
    void findByUsername_WhenUserExists_ShouldReturnUserDto() {
        // Given
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When
        Optional<UserDto> result = userService.findByUsername("testuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userDto);
        verify(userRepository).findByUsername("testuser");
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void findByUsername_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findByUsername("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findByUsername("nonexistent");
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should find user by email when user exists")
    void findByEmail_WhenUserExists_ShouldReturnUserDto() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When
        Optional<UserDto> result = userService.findByEmail("test@example.com");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userDto);
        verify(userRepository).findByEmail("test@example.com");
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void findByEmail_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findByEmail("nonexistent@example.com");
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should return true when username exists")
    void existsByUsername_WhenUsernameExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // When
        Boolean result = userService.existsByUsername("testuser");

        // Then
        assertThat(result).isTrue();
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    @DisplayName("Should return false when username does not exist")
    void existsByUsername_WhenUsernameDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        // When
        Boolean result = userService.existsByUsername("nonexistent");

        // Then
        assertThat(result).isFalse();
        verify(userRepository).existsByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // When
        Boolean result = userService.existsByEmail("test@example.com");

        // Then
        assertThat(result).isTrue();
        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmail_WhenEmailDoesNotExist_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        // When
        Boolean result = userService.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(result).isFalse();
        verify(userRepository).existsByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should save user successfully")
    void save_ShouldSaveAndReturnUserDto() {
        // Given
        UserDto newUserDto = new UserDto();
        newUserDto.setUsername("newuser");
        newUserDto.setEmail("newuser@example.com");
        newUserDto.setPhone("0987654321");

        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setEmail("newuser@example.com");
        newUser.setPhone("0987654321");

        User savedUser = new User();
        savedUser.setId(2);
        savedUser.setUsername("newuser");
        savedUser.setEmail("newuser@example.com");
        savedUser.setPhone("0987654321");

        UserDto savedUserDto = new UserDto();
        savedUserDto.setId(2);
        savedUserDto.setUsername("newuser");
        savedUserDto.setEmail("newuser@example.com");
        savedUserDto.setPhone("0987654321");

        when(userMapper.toEntity(newUserDto)).thenReturn(newUser);
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(savedUserDto);

        // When
        UserDto result = userService.save(newUserDto);

        // Then
        assertThat(result).isEqualTo(savedUserDto);
        assertThat(result.getId()).isEqualTo(2);
        verify(userMapper).toEntity(newUserDto);
        verify(userRepository).save(newUser);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    @DisplayName("Should find user by ID when user exists")
    void findById_WhenUserExists_ShouldReturnUserDto() {
        // Given
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        // When
        Optional<UserDto> result = userService.findById(1);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(userDto);
        verify(userRepository).findById(1);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void findById_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findById(999);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(999);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should return null when user is null")
    void findById_WhenUserIsNull_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<UserDto> result = userService.findById(999);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(999);
        verifyNoInteractions(userMapper);
    }
} 
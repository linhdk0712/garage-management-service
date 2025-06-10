package vn.utc.service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.dtos.UserPrincipal;
import vn.utc.service.entity.User;
import vn.utc.service.mapper.UserMapper;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomUserDetailsService Unit Tests")
class CustomUserDetailsServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setCreatedAt(Instant.now());

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setPassword("encodedPassword");
        userDto.setActive(true);
        userDto.setCreatedAt(Instant.now());
    }

    @Test
    @DisplayName("Should load user by username when user exists")
    void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        when(userMapper.toEntity(userDto)).thenReturn(user);

        // When
        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userService).findByUsername("testuser");
        verify(userService).save(any(UserDto.class));
        verify(userMapper).toEntity(userDto);
    }

    @Test
    @DisplayName("Should update last login time when loading user")
    void loadUserByUsername_ShouldUpdateLastLoginTime() {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userService.save(any(UserDto.class))).thenAnswer(invocation -> {
            UserDto savedUser = invocation.getArgument(0);
            assertThat(savedUser.getLastLogin()).isNotNull();
            return savedUser;
        });
        when(userMapper.toEntity(userDto)).thenReturn(user);

        // When
        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        verify(userService).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Should throw exception when user not found by username")
    void loadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username or email: nonexistent");
        verify(userService).findByUsername("nonexistent");
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should load user by ID when user exists")
    void loadUserById_WhenUserExists_ShouldReturnUserDetails() {
        // Given
        when(userService.findById(1)).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);

        // When
        UserDetails result = customUserDetailsService.loadUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userService).findById(1);
        verify(userMapper).toEntity(userDto);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void loadUserById_WhenUserNotFound_ShouldThrowException() {
        // Given
        when(userService.findById(999)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserById(999L))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with id: 999");
        verify(userService).findById(999);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should handle null username")
    void loadUserByUsername_WithNullUsername_ShouldThrowException() {
        // Given
        when(userService.findByUsername(null)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(null))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username or email: null");
        verify(userService).findByUsername(null);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should handle empty username")
    void loadUserByUsername_WithEmptyUsername_ShouldThrowException() {
        // Given
        when(userService.findByUsername("")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> customUserDetailsService.loadUserByUsername(""))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with username or email: ");
        verify(userService).findByUsername("");
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Should create UserPrincipal with correct user data")
    void loadUserByUsername_ShouldCreateUserPrincipalWithCorrectData() {
        // Given
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");
        userDto.setActive(true);
        
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        when(userMapper.toEntity(userDto)).thenReturn(user);

        // When
        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isInstanceOf(UserPrincipal.class);
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userMapper).toEntity(userDto);
    }

    @Test
    @DisplayName("Should handle user with inactive status")
    void loadUserByUsername_WithInactiveUser_ShouldStillLoadUser() {
        // Given
        userDto.setActive(false);
        
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userService.save(any(UserDto.class))).thenReturn(userDto);
        when(userMapper.toEntity(userDto)).thenReturn(user);

        // When
        UserDetails result = customUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        verify(userService).findByUsername("testuser");
        verify(userService).save(any(UserDto.class));
        verify(userMapper).toEntity(userDto);
    }
} 
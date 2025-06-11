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
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.CustomerRegister;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.User;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerService Unit Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;
    private User user;
    private UserDto userDto;
    private CustomerRegister customerRegister;

    @BeforeEach
    void setUp() {
        // Setup test data
        user = new User();
        user.setId(1);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        userDto = new UserDto();
        userDto.setId(1);
        userDto.setUsername("testuser");
        userDto.setEmail("test@example.com");

        customer = new Customer()
                .setId(1)
                .setUser(user)
                .setFirstName("John")
                .setLastName("Doe")
                .setAddress("123 Test St")
                .setCity("Test City")
                .setState("Test State");

        customerDto = new CustomerDto(1, "John", "Doe", "123 Test St", "Test City", "Test State", null, null, null);

        customerRegister = new CustomerRegister()
                .setFirstName("Jane")
                .setLastName("Smith")
                .setAddress("456 New St")
                .setCity("New City")
                .setState("New State");
    }

    @Test
    @DisplayName("Should find customer by ID when customer exists")
    void findByCustomerId_WhenCustomerExists_ShouldReturnCustomerDto() {
        // Given
        when(userService.findById(1)).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(customerRepository.findCustomerByUser(user)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Optional<CustomerDto> result = customerService.findByCustomerId(1);

        // Then
        assertThat(result).isPresent();
    assertThat(result).contains(customerDto);
        verify(userService).findById(1);
        verify(userMapper).toEntity(userDto);
        verify(customerRepository).findCustomerByUser(user);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return empty when user not found by ID")
    void findByCustomerId_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findById(999)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.findByCustomerId(999);

        // Then
        assertThat(result).isEmpty();
        verify(userService).findById(999);
        verifyNoInteractions(userMapper, customerRepository, customerMapper);
    }

    @Test
    @DisplayName("Should return empty when customer not found for user")
    void findByCustomerId_WhenCustomerNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findById(1)).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(customerRepository.findCustomerByUser(user)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.findByCustomerId(1);

        // Then
        assertThat(result).isEmpty();
        verify(userService).findById(1);
        verify(userMapper).toEntity(userDto);
        verify(customerRepository).findCustomerByUser(user);
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("Should save customer successfully")
    void saveCustomer_ShouldSaveAndReturnCustomerDto() {
        // Given
        Customer savedCustomer = new Customer()
                .setId(2)
                .setUser(user)
                .setFirstName("Jane")
                .setLastName("Smith")
                .setAddress("456 New St")
                .setCity("New City")
                .setState("New State");

        CustomerDto savedCustomerDto = new CustomerDto(2, "Jane", "Smith", "456 New St", "New City", "New State", null, null, null);

        when(customerMapper.toEntityFromRegister(customerRegister)).thenReturn(savedCustomer);
        when(customerRepository.save(savedCustomer)).thenReturn(savedCustomer);
        when(customerMapper.toDto(savedCustomer)).thenReturn(savedCustomerDto);

        // When
        CustomerDto result = customerService.saveCustomer(customerRegister);

        // Then
        assertThat(result).isEqualTo(savedCustomerDto);
        verify(customerMapper).toEntityFromRegister(customerRegister);
        verify(customerRepository).save(savedCustomer);
        verify(customerMapper).toDto(savedCustomer);
    }

    @Test
    @DisplayName("Should find customer by username when customer exists")
    void findByUserName_WhenCustomerExists_ShouldReturnCustomerDto() {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(customerRepository.findCustomerByUser(user)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Optional<CustomerDto> result = customerService.finByUserName("testuser");

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(customerDto);
        verify(userService).findByUsername("testuser");
        verify(userMapper).toEntity(userDto);
        verify(customerRepository).findCustomerByUser(user);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return empty when user not found by username")
    void findByUserName_WhenUserNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.finByUserName("nonexistent");

        // Then
        assertThat(result).isEmpty();
        verify(userService).findByUsername("nonexistent");
        verifyNoInteractions(userMapper, customerRepository, customerMapper);
    }

    @Test
    @DisplayName("Should return empty when customer not found for username")
    void findByUserName_WhenCustomerNotFound_ShouldReturnEmpty() {
        // Given
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(userDto));
        when(userMapper.toEntity(userDto)).thenReturn(user);
        when(customerRepository.findCustomerByUser(user)).thenReturn(Optional.empty());

        // When
        Optional<CustomerDto> result = customerService.finByUserName("testuser");

        // Then
        assertThat(result).isEmpty();
        verify(userService).findByUsername("testuser");
        verify(userMapper).toEntity(userDto);
        verify(customerRepository).findCustomerByUser(user);
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("Should return all customers")
    void findAllCustomers_ShouldReturnAllCustomers() {
        // Given
        List<Customer> customers = List.of(customer);
        List<CustomerDto> customerDtos = List.of(customerDto);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        List<CustomerDto> result = customerService.findAllCustomers();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(customerDtos);
        verify(customerRepository).findAll();
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return empty list when no customers exist")
    void findAllCustomers_WhenNoCustomers_ShouldReturnEmptyList() {
        // Given
        when(customerRepository.findAll()).thenReturn(List.of());

        // When
        List<CustomerDto> result = customerService.findAllCustomers();

        // Then
        assertThat(result).isEmpty();
        verify(customerRepository).findAll();
        verifyNoInteractions(customerMapper);
    }

    @Test
    @DisplayName("Should return paginated customers")
    void findAllCustomers_WithPageable_ShouldReturnPaginatedCustomers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        Page<CustomerDto> expectedPage = new PageImpl<>(List.of(customerDto), pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Page<CustomerDto> result = customerService.findAllCustomers(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerRepository).findAll(pageable);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return filtered customers when search and status provided")
    void findAllCustomers_WithSearchAndStatus_ShouldReturnFilteredCustomers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "test";
        String status = "ACTIVE";
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        Page<CustomerDto> expectedPage = new PageImpl<>(List.of(customerDto), pageable, 1);

        when(customerRepository.findBySearchAndStatus(search, status, pageable)).thenReturn(customerPage);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Page<CustomerDto> result = customerService.findAllCustomers(pageable, search, status);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerRepository).findBySearchAndStatus(search, status, pageable);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return all customers when search and status are empty")
    void findAllCustomers_WithEmptySearchAndStatus_ShouldReturnAllCustomers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String search = "";
        String status = "";
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        Page<CustomerDto> expectedPage = new PageImpl<>(List.of(customerDto), pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Page<CustomerDto> result = customerService.findAllCustomers(pageable, search, status);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerRepository).findAll(pageable);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Should return all customers when search and status are null")
    void findAllCustomers_WithNullSearchAndStatus_ShouldReturnAllCustomers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Customer> customers = List.of(customer);
        Page<Customer> customerPage = new PageImpl<>(customers, pageable, 1);
        Page<CustomerDto> expectedPage = new PageImpl<>(List.of(customerDto), pageable, 1);

        when(customerRepository.findAll(pageable)).thenReturn(customerPage);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        // When
        Page<CustomerDto> result = customerService.findAllCustomers(pageable, null, null);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        assertThat(result.getContent()).hasSize(1);
        verify(customerRepository).findAll(pageable);
        verify(customerMapper).toDto(customer);
    }
} 
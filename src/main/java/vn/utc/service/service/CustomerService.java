package vn.utc.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.CustomerDto;
import vn.utc.service.dtos.CustomerRegister;
import vn.utc.service.dtos.UserDto;
import vn.utc.service.entity.Customer;
import vn.utc.service.entity.User;
import vn.utc.service.exception.CustomerNotFoundException;
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final UserService userService;
  private final UserMapper userMapper;

  public Optional<CustomerDto> findByCustomerId(int id) {
    UserDto userDtoOpt =
        userService
            .findById(id)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    User user = userMapper.toEntity(userDtoOpt);
    Optional<Customer> customerOpt = customerRepository.findCustomerByUser(user);

    return customerOpt.map(customerMapper::toDto);
  }
  public Optional<CustomerDto> findByCustomerUserId(int id) {

    Optional<Customer> customerOpt = customerRepository.findCustomerById(id);

    return customerOpt.map(customerMapper::toDto);
  }

  public CustomerDto saveCustomer(CustomerRegister customerRegister) {
    Customer customer = customerMapper.toEntityFromRegister(customerRegister);
    return customerMapper.toDto(customerRepository.save(customer));
  }

  public Optional<CustomerDto> finByUserName(String userName) {
    UserDto userDtoOpt =
        userService
            .findByUsername(userName)
            .orElseThrow(
                () ->
                    new CustomerNotFoundException("Customer not found with userName: " + userName));
    ;
    User user = userMapper.toEntity(userDtoOpt);
    Optional<Customer> customerOpt = customerRepository.findCustomerByUser(user);

    return customerOpt.map(customerMapper::toDto);
  }

  public List<CustomerDto> findAllCustomers() {
    List<Customer> customers = customerRepository.findAll();
    return customers.stream().map(customerMapper::toDto).toList();
  }

  public Page<CustomerDto> findAllCustomers(Pageable pageable) {
    return customerRepository.findAll(pageable).map(customerMapper::toDto);
  }

  public Page<CustomerDto> findAllCustomers(Pageable pageable, String search, String status) {
    if (search != null && !search.trim().isEmpty() || status != null && !status.trim().isEmpty()) {
      return customerRepository
          .findBySearchAndStatus(search, status, pageable)
          .map(customerMapper::toDto);
    }
    return customerRepository.findAll(pageable).map(customerMapper::toDto);
  }

  /**
   * Save multiple customers for data initialization purposes
   *
   * @param customers List of Customer entities to save
   * @return List of saved Customer entities
   */
  @Transactional
  public List<Customer> saveAllForInitialization(List<Customer> customers) {
    return customerRepository.saveAll(customers);
  }

  /**
   * Find customer by user for data initialization purposes
   *
   * @param user The user to search for
   * @return Optional containing the Customer entity if found
   */
  public Optional<Customer> findCustomerByUserForInitialization(User user) {
    return customerRepository.findCustomerByUser(user);
  }
}

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
import vn.utc.service.mapper.CustomerMapper;
import vn.utc.service.mapper.UserMapper;
import vn.utc.service.repo.CustomerRepository;

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
       UserDto userDto = userService.findById(id).orElse(null);
       Customer customer = customerRepository.findCustomerByUser(userMapper.toEntity(userDto)).orElse(null);
        return Optional.ofNullable(customerMapper.toDto(customer));
    }

    public  CustomerDto saveCustomer(CustomerRegister customerRegister) {
        Customer customer = customerMapper.toEntityFromRegister(customerRegister);
        return customerMapper.toDto(customerRepository.save(customer));
    }
    public Optional<CustomerDto> finByUserName(String userName) {
        UserDto userDto = userService.findByUsername(userName).orElse(null);
        if (userDto == null) {
            return Optional.empty();
        }
        Customer customer = customerRepository.findCustomerByUser(userMapper.toEntity(userDto)).orElse(null);
        return Optional.ofNullable(customerMapper.toDto(customer));
    }
    public List<CustomerDto> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::toDto)
                .toList();
    }

    public Page<CustomerDto> findAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toDto);
    }

    public Page<CustomerDto> findAllCustomers(Pageable pageable, String search, String status) {
        if (search != null && !search.trim().isEmpty() || status != null && !status.trim().isEmpty()) {
            return customerRepository.findBySearchAndStatus(search, status, pageable)
                    .map(customerMapper::toDto);
        }
        return customerRepository.findAll(pageable)
                .map(customerMapper::toDto);
    }

}

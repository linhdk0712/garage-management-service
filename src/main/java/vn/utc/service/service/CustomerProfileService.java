package vn.utc.service.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.utc.service.dtos.CustomerProfileDto;
import vn.utc.service.mapper.CustomerProfileMapper;
import vn.utc.service.repo.CustomerProfileRepository;

@Service
@RequiredArgsConstructor
public class CustomerProfileService {
  private final CustomerProfileRepository customerProfileRepository;
  private final CustomerProfileMapper customerProfileMapper;

  public List<CustomerProfileDto> findAllCustomerProfiles() {
    return customerProfileRepository.findAll().stream()
        .map(customerProfileMapper::toDto)
        .toList();
  }

  public Optional<CustomerProfileDto> findCustomerProfileByUsername(String username) {
    return customerProfileRepository
        .findCustomerProfileByUsername(username)
        .map(customerProfileMapper::toDto);
  }
}

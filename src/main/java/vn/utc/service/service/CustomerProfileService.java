package vn.utc.service.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  public Page<CustomerProfileDto> findAllCustomerProfiles(Pageable pageable) {
    return customerProfileRepository.findAll(pageable)
        .map(customerProfileMapper::toDto);
  }

  public Page<CustomerProfileDto> findAllCustomerProfiles(Pageable pageable, String search, String status) {
    if (search != null && !search.trim().isEmpty() || status != null && !status.trim().isEmpty()) {
      return customerProfileRepository.findBySearchAndStatus(search, status, pageable)
          .map(customerProfileMapper::toDto);
    }
    return customerProfileRepository.findAll(pageable)
        .map(customerProfileMapper::toDto);
  }

  public Optional<CustomerProfileDto> findCustomerProfileByUsername(String username) {
    return customerProfileRepository
        .findCustomerProfileByUsername(username)
        .map(customerProfileMapper::toDto);
  }
}

package ru.prusov.TelegramBotConstructionHelper.financeanalizator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Customer;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository.CustomerRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Optional<Customer> getCustomerByName(String name){
        return customerRepository.findCustomerContainsName(name);
    }
}

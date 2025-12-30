package ru.prusov.TelegramBotConstructionHelper.financeanalizator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contractor;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository.ContractorRepository;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractorService {
    private final ContractorRepository contractorRepository;

    public Optional<Contractor> getContractorByName(String name){
        return contractorRepository.findCustomerByName(name);
    }
}

package ru.prusov.TelegramBotConstructionHelper.financeanalizator.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractWithTotalPaymentsDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;

import java.util.List;
import java.util.Optional;

@RestController

@RequestMapping("/api/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public Page<ContractDTO> getAllContracts(Pageable pageable) {
        return contractService.findAll(pageable);
    }

    @GetMapping("/with-payments")
    public Page<ContractWithTotalPaymentsDTO> getContracts(Pageable pageable) {
        return contractService.getAllContractDto(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContractById(@PathVariable("id") Long id) {
        Optional<ContractDTO> contract = contractService.findContractDtoById(id);
        return contract.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-payments")
    public ContractWithTotalPaymentsDTO getContractWithTotalPaymentsById(@PathVariable("id") Long id) {
        return contractService.getContractDto(id);
    }

    @PostMapping
    public Contract saveContract(@Valid @RequestBody Contract contract) {
        return contractService.saveContract(contract);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(@PathVariable("id") Long id,
                                                   @Valid @RequestBody Contract contractDetails) {
        Optional<Contract> contract = contractService.findContractById(id);
        if (contract.isPresent()) {
            Contract existedContract = contract.get();
            existedContract.setContractNumber(contractDetails.getContractNumber());
            existedContract.setDescription(contractDetails.getDescription());
            existedContract.setCustomer(contractDetails.getCustomer());
            existedContract.setContractor(contractDetails.getContractor());
            existedContract.setStartDate(contractDetails.getStartDate());
            existedContract.setEndDate(contractDetails.getEndDate());
            existedContract.setIsCompleted(contractDetails.getIsCompleted());
            existedContract.setContractAmount(contractDetails.getContractAmount());
            return ResponseEntity.ok(existedContract);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractById(@PathVariable("id") Long id) {

        if (contractService.findContractById(id).isPresent()) {
            contractService.deleteContractById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

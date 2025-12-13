package ru.prusov.TelegramBotConstructionHelper.financeanalizator.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractWithTotalPaymentsDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository.ContractRepository;

import java.util.List;
import java.util.Optional;

@Service

public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        Page<ContractDTO> contractDTOS = contractRepository.findAll(pageable)
                .map(this::convertToDto);
        return contractDTOS;
    }

    private ContractDTO convertToDto(Contract contract) {
        return new ContractDTO(
                contract.getId(),
                contract.getContractNumber(),
                contract.getDescription(),
                contract.getCustomer() != null ? contract.getCustomer().getName() : null,
                contract.getContractor() != null ? contract.getContractor().getName() : null,
                contract.getContractAmount(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getIsCompleted()
        );
    }

    @Transactional(readOnly = true)
    public Optional<Contract> findContractById(Long id) {
        return contractRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ContractDTO> findContractDtoById(Long id) {
        return contractRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public Contract saveContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Transactional
    public void deleteContractById(Long id) {
        contractRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ContractWithTotalPaymentsDTO> getAllContractDto(Pageable pageable) {
        return contractRepository.getAllContractDto(pageable);
    }

    @Transactional(readOnly = true)
    public ContractWithTotalPaymentsDTO getContractDto(Long contractId) {
        return contractRepository.findContractWithTotalPaymentsById(contractId);
    }

    @Transactional
    public void deleteByContractNumber(String contractNumber){
        contractRepository.deleteByContractNumber(contractNumber);
    }


    public Contract convertToContract(ContractDTOFull contractDTO){
        Contract contract = new Contract();
        contract.setContractNumber(contractDTO.getContractNumber());
        contract.setDescription(contractDTO.getDescription());
        contract.setContractAmount(contractDTO.getContractAmount());
        contract.setContractor(contractDTO.getContractor());
        contract.setCustomer(contractDTO.getCustomer());
        contract.setStartDate(contractDTO.getStartDate());
        contract.setEndDate(contractDTO.getEndDate());
        contract.setIsCompleted(false);
        return contract;
    }
    @Transactional(readOnly = true)
    public Optional<Contract> getContractByContractNumber(String messageText) {
        return contractRepository.findByContractNumber(messageText);
    }
}
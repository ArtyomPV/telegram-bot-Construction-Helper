package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractWithTotalPaymentsDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    @Query("SELECT new ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractWithTotalPaymentsDTO(" +
            "c.id, c.contractNumber, c.description, cust.name, contr.name, " +
            "c.contractAmount, c.startDate, c.endDate, c.isCompleted, " +
            "COALESCE(SUM(p.amount), 0)) " +
            "FROM Contract c " +
            "LEFT JOIN c.customer cust " +
            "LEFT JOIN c.contractor contr " +
            "LEFT JOIN Payment p ON p.contract = c " +
            "GROUP BY c.id, c.contractNumber, c.description, cust.name, contr.name, " +
            "c.contractAmount, c.startDate, c.endDate, c.isCompleted")
    List<ContractWithTotalPaymentsDTO> getAllContractDto();

    @Query("SELECT new ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractWithTotalPaymentsDTO(" +
            "c.id, c.contractNumber, c.description, cust.name, contr.name, " +
            "c.contractAmount, c.startDate, c.endDate, c.isCompleted, " +
            "COALESCE(SUM(p.amount), 0)) " +
            "FROM Contract c " +
            "LEFT JOIN c.customer cust " +
            "LEFT JOIN c.contractor contr " +
            "LEFT JOIN Payment p ON p.contract = c " +
            "where c.id = :contractId " +
            "GROUP BY c.id, c.contractNumber, c.description, cust.name, contr.name, " +
            "c.contractAmount, c.startDate, c.endDate, c.isCompleted")
    ContractWithTotalPaymentsDTO findContractWithTotalPaymentsById(Long contractId);
}

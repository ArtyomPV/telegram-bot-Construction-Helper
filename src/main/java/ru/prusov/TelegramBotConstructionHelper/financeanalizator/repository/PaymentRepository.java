package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByContractId(Long contractId);
}

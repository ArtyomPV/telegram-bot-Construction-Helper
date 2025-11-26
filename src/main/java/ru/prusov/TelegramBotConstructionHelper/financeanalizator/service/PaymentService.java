package ru.prusov.TelegramBotConstructionHelper.financeanalizator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Payment;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository.PaymentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public List<PaymentDTO> findAll() {
        return paymentRepository.findAll().stream()
                .map(this::ConvertToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Transactional
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Transactional
    public void deleteByID(Long id) {
        paymentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> findPaymentsByContractId(Long contractId) {
        return paymentRepository.findByContractId(contractId).stream()
                .map(this::ConvertToDto)
                .toList();
    }

    private PaymentDTO ConvertToDto(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getPaymentType(),
                payment.getAmount(),
                payment.getPaymentDate(),
                payment.getDescription(),
                payment.getContract().getId(),
                payment.getContract().getContractNumber()
        );
    }

}
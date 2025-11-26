package ru.prusov.TelegramBotConstructionHelper.financeanalizator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.PaymentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;


    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.findAll();
    }

    @GetMapping("/{id}")
    public List<PaymentDTO> getPaymentsByContractId(@PathVariable("id") Long contractId) {
        return paymentService.findPaymentsByContractId(contractId);
    }
}

package ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private PaymentType paymentType;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private String description;
    private Long contractId;
    private String contractNumber;
}

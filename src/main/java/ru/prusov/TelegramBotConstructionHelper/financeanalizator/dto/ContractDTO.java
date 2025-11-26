package ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ContractDTO {
    private Long id;
    private String contractNumber;
    private String description;
    private String customerName;
    private String contractorName;
    private BigDecimal contractAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCompleted;

}
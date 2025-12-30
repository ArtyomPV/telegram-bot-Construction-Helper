package ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contractor;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContractDTOFull {

        private Long id;
        private String contractNumber;
        private String description;
        private Customer customer;
        private Contractor contractor;
        private BigDecimal contractAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isCompleted;
}

package ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@Getter
@Setter
public class PaymentDtoComponent {

        private Long id;
        private PaymentType paymentType;
        private BigDecimal amount;
        private LocalDate paymentDate;
        private String description;
        private Long contractId;
        private String contractNumber;
}

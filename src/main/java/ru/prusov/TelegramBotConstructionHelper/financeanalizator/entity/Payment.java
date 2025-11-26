package ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;

    @NotNull(message = "Amount is mandatory")
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Payment date is mandatory")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "description")
    private String description;

    @NotNull(message = "Contract is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

}
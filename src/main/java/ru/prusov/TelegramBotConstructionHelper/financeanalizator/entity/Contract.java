package ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Contract number is mandatory")
    @Column(name = "contract_number", unique = true, nullable = false)
    private String contractNumber;

    @NotBlank(message = "Summary is mandatory")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Customer is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Contractor is mandatory")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractor_id", nullable = false)
    private Contractor contractor;

    @NotNull(message = "Contract amount is mandatory")
    @Column(name = "contract_amount", nullable = false)
    private BigDecimal contractAmount;

    @NotNull(message = "Start date is mandatory")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is mandatory")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_completed", nullable = false)
    private Boolean isCompleted = false;

}
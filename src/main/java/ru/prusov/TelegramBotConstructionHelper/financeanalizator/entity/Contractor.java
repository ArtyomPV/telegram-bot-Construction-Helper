package ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "contractors")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Contractor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "contact_info")
    private String contactInfo;

}
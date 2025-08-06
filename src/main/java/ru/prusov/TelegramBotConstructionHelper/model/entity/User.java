package ru.prusov.TelegramBotConstructionHelper.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.prusov.TelegramBotConstructionHelper.usecase.role.Role;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long chatId;
    private String username;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserState state = UserState.NONE;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}

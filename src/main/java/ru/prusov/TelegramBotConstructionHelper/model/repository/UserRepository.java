package ru.prusov.TelegramBotConstructionHelper.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.prusov.TelegramBotConstructionHelper.model.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> getUserByChatId(Long chatId);
}

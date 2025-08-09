package ru.prusov.TelegramBotConstructionHelper.model.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.prusov.TelegramBotConstructionHelper.model.entity.User;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> getUserByChatId(Long chatId);
     @Modifying
     @Transactional
     @Query("Update User u set u.state = :userState where u.chatId = :id")
     void setUserStateByChatId(@Param("id") Long chatId, @Param("userState") UserState userState);

     @Transactional
     @Query("SELECT u.state FROM User u WHERE u.chatId = :chatId ")
     UserState getUserStateByChatId(@Param("chatId") long chatId);
}


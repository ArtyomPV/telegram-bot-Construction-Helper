package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.User;
import ru.prusov.TelegramBotConstructionHelper.model.repository.UserRepository;
import ru.prusov.TelegramBotConstructionHelper.usecase.role.Role;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Long adminChatId;


    @Transactional
    public User findOrCreateUser(Long chatId, String username){
        return userRepository.getUserByChatId(chatId).orElseGet( () -> {
           User user = new User();
           user.setChatId(chatId);
           user.setUsername(username);
           if( chatId.equals(adminChatId)) {
               user.setRole(Role.ADMIN);
           } else {
               user.setRole(Role.VISITOR);
           }
            return userRepository.save(user);
        });
    }

    public User getUserByChatId(Long chatId){
        return userRepository.getUserByChatId(chatId).get();
    }
}

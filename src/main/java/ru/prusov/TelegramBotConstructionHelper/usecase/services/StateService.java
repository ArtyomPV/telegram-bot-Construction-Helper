package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.repository.UserRepository;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

@Service
@RequiredArgsConstructor
public class StateService {
    private final UserRepository userRepository;
    @Transactional
    public void setUserStateByChatId(Long chatId, UserState userState){
        userRepository.setUserStateByChatId(chatId, userState);
    }

    @Transactional
    public void clearUserStateByChatId(long chatId){
        userRepository.setUserStateByChatId(chatId, UserState.NONE);
    }

@Transactional(readOnly = true)
    public UserState getUserStateByChatId(Long chatId) {
        return userRepository.getUserStateByChatId(chatId);
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetUpdateContractOptionsState extends AbstractState {
    private String CONTENT_TEXT = "Введите новое значение";
    private final StateService stateService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        UserState userState = getUserStateFromMessage(messageText);

        stateService.setUserStateByChatId(chatId, userState);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }

    private UserState getUserStateFromMessage(String messageText) {
        messageText = messageText.trim();
        switch (messageText.toLowerCase()) {
            case "стоимость договора" -> {
                return UserState.WAITING_UPDATE_CONTRACT_AMOUNT;
            }
            case "начало работ" -> {
                return UserState.WAITING_UPDATE_CONTRACT_START;
            }
            case "завершение работ" -> {
                return UserState.WAITING_UPDATE_CONTRACT_END;
            }
            case "описание договора" -> {
                return UserState.WAITING_UPDATE_CONTRACT_DESCRIPTION;
            }
            default -> {
                return null;
            }
        }
    }


    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return UserState.WAITING_UPDATE_OPTIONS_CONTRACT;
    }
}

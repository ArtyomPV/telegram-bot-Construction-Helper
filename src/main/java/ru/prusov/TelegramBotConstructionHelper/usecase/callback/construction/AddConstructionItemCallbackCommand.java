package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_TITLE_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ADD_CONSTRUCTION_ITEM;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddConstructionItemCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final StateService stateService;

    @Override
    public String command() {
        return ADD_CONSTRUCTION_ITEM;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_TITLE_CONSTRUCTION_ITEM);
        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId(),
                ASK_TITLE_CONSTRUCTION_ITEM
        );

        try {
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Request failed: object name - class {}",
                    AddConstructionItemCallbackCommand.class.getSimpleName());
        }
    }
}

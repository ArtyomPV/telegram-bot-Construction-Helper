package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.CONSTRUCTION_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConstructionCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final StateService stateService;

    @Override
    public String command() {
        return CallbackData.CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        log.info("started command {}", command());
        stateService.clearUserStateByChatId(commonInfo.getChatId());
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                CONSTRUCTION_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Реализованные объекты строительства", "Интересные статьи", "Связаться с нами", "Назад"),
                        List.of(1, 1, 1, 1),
                        List.of(REALIZED_CONSTRUCTION, ARTICLE_CONSTRUCTION, CONTACT_CONSTRUCTION, START)
                ));
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

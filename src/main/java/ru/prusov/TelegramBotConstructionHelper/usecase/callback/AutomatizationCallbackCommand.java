package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.AUTOMATIZATION_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.CONSTRUCTION_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.AUTOMATIZATION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutomatizationCallbackCommand implements CallbackCommand {
    private final TelegramClient client;

    @Override
    public String command() {
        return AUTOMATIZATION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        log.info("started command {}", command());
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                AUTOMATIZATION_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(START)
                ));
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

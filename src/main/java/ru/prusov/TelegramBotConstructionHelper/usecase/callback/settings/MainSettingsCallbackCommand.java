package ru.prusov.TelegramBotConstructionHelper.usecase.callback.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.MAIN_SETTINGS_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainSettingsCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final StateService stateService;

    @Override
    public String command() {
        return SETTINGS;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.clearUserStateByChatId(chatId);
        DeleteMessage deleteMessage1 = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 1);
        DeleteMessage deleteMessage2 = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId());
        SendMessage sendMessageText = AnswerMethodFactory.getSendMessage(
                chatId,
                MAIN_SETTINGS_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Сменить логотип", "Добавить завершенный объект", "Добавить статью", "Назад"),
                        List.of(1, 1, 1, 1),
                        List.of(LOGO_CHANGE, ADD_CONSTRUCTION_ITEM, ADD_ARTICLE, START)
                )
        );
        try {
            client.execute(deleteMessage1);
            client.execute(deleteMessage2);
            client.execute(sendMessageText);
        } catch (TelegramApiException e) {
            log.error("Не выполнен метод {}", MainSettingsCallbackCommand.class.getSimpleName());
            throw new RuntimeException(e);
        }
    }
}


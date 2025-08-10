package ru.prusov.TelegramBotConstructionHelper.usecase.callback.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

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
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 1);
        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                chatId,
                commonInfo.getMessageId(),
                MAIN_SETTINGS_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Сменить логотип", "Добавить завершенный объект", "Назад"),
                        List.of(1, 1, 1),
                        List.of(LOGO_CHANGE, ADD_CONSTRUCTION_ITEM, START)
                )
        );
        try {
            client.execute(deleteMessage);
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Не выполнен метод {}", MainSettingsCallbackCommand.class.getSimpleName());
            throw new RuntimeException(e);
        }
    }
}


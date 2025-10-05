package ru.prusov.TelegramBotConstructionHelper.usecase.callback.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.MAIN_SETTINGS_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MainSettingsCallbackCommand extends AbstractCallbackCommand {

    private final StateService stateService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        stateService.clearUserStateByChatId(chatId);

        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                MAIN_SETTINGS_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Сменить логотип", "Добавить завершенный объект", "Добавить статью", "Назад"),
                        List.of(1, 1, 1, 1),
                        List.of(LOGO_CHANGE, ADD_CONSTRUCTION_ITEM, ADD_ARTICLE, START)),
                commonInfo.getMessageId() + 1
        );
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return SETTINGS;
    }
}


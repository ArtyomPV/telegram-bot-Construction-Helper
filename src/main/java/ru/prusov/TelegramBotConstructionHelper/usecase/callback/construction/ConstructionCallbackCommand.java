package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.CONSTRUCTION_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConstructionCallbackCommand extends AbstractCallbackCommand {

    private final StateService stateService;

    @Override
    public String command() {
        return CallbackData.CONSTRUCTION;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.clearUserStateByChatId(chatId);

        deleteAllMessage(chatId);

        replyAndTrack(commonInfo.getChatId(),
                CONSTRUCTION_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Реализованные объекты строительства", "Интересные статьи", "Связаться с нами", "Назад"),
                        List.of(1, 1, 1, 1),
                        List.of(REALIZED_CONSTRUCTION, ARTICLE_CONSTRUCTION, CONTACT_CONSTRUCTION, START)),
                commonInfo.getMessageId() + 1
                );
    }

    @Override
    protected Logger log() {
        return log;
    }
}

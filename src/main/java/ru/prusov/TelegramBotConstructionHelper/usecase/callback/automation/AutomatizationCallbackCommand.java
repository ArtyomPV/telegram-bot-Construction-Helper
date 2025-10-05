package ru.prusov.TelegramBotConstructionHelper.usecase.callback.automation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.AUTOMATIZATION_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutomatizationCallbackCommand extends AbstractCallbackCommand {

    @Override
    public String command() {
        return AUTOMATIZATION;
    }


    @Override
    protected void doExecute(CommonInfo commonInfo) {
        deleteAllMessage(commonInfo.getChatId());

        replyAndTrack(commonInfo.getChatId(),
                AUTOMATIZATION_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Реализованные объекты", "Интересные статьи", "Связаться с нами","Назад"),
                        List.of(1,1,1,1),
                        List.of(REALIZED_AUTOMATICS, ARTICLE_AUTOMATICS, CONTACT_AUTOMATICS, START)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

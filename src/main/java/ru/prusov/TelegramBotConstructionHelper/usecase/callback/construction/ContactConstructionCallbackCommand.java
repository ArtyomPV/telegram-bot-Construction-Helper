package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.CONSTRUCTION_MENU_CONTACTS;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactConstructionCallbackCommand extends AbstractCallbackCommand {

    @Override
    public String command() {
        return CONTACT_CONSTRUCTION;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        deleteAllMessage(commonInfo.getChatId());

        replyAndTrack(commonInfo.getChatId(),
                String.format(CONSTRUCTION_MENU_CONTACTS, "Артем"),
                KeyboardFactory.getInlineKeyboard(
                        List.of("В главное меню", "Назад"),
                        List.of(1, 1),
                        List.of(START, CONSTRUCTION)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

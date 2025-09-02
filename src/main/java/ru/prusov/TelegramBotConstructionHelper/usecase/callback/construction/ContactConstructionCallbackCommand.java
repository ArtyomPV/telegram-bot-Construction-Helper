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

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.CONSTRUCTION_MENU_CONTACTS;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContactConstructionCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    @Override
    public String command() {
        return CONTACT_CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                String.format(CONSTRUCTION_MENU_CONTACTS, "Артем"),
                KeyboardFactory.getInlineKeyboard(
                        List.of("В главное меню", "Назад"),
                        List.of(1, 1),
                        List.of(START, CONSTRUCTION)
                )
        );
        sendMessage.enableHtml(true);

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Don`t execute method: {}, {}", ContactConstructionCallbackCommand.class.getSimpleName() ,e.getMessage());
        }
    }
}

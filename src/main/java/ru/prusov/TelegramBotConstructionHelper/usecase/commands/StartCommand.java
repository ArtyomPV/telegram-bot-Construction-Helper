package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.constants.TextConstants;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.UserService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.LOGO_IMAGE_PATH;
import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.RESOURCE_PATH;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command{
    private final TelegramClient client;
    private final UserService userService;
    @Override
    public UserCommand command() {
        return UserCommand.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        userService.findOrCreateUser(chatId, commonInfo.getUserFromTelegram().getFirstName());
        SendPhoto sendPhoto = AnswerMethodFactory.getSendPhoto(chatId, RESOURCE_PATH + LOGO_IMAGE_PATH);
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId,
                TextConstants.START_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Строительство", "Инженерные сети", "Автоматика и система управления"),
                        List.of(1,1,1),
                        List.of(CONSTRUCTION, ENGINEERING, AUTOMATIZATION)
                ));
        try {
//            client.execute(sendPhoto);
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

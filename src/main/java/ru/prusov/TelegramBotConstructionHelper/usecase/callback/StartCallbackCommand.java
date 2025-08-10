package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.constants.TextConstants;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Photo;
import ru.prusov.TelegramBotConstructionHelper.model.entity.User;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.PhotoService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.UserService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.LOGO_IMAGE_PATH;
import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.RESOURCE_PATH;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.role.Role.ADMIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCallbackCommand implements CallbackCommand{
    private final TelegramClient client;
    private final UserService userService;
    private final PhotoService photoService;

    @Override
    public String command() {
        return CallbackData.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        User user = userService.findOrCreateUser(chatId, commonInfo.getUserFromTelegram().getFirstName());
        InlineKeyboardMarkup inlineKeyboard = null;
        if (user.getRole().equals(ADMIN)) {
            log.info(user.getRole().toString());
            inlineKeyboard = KeyboardFactory.getInlineKeyboard(
                    List.of("Строительство", "Инженерные сети", "Автоматика и система управления", "Настройки"),
                    List.of(1, 1, 1, 1),
                    List.of(CONSTRUCTION, ENGINEERING, AUTOMATIZATION, SETTINGS)
            );
        } else {
            inlineKeyboard = KeyboardFactory.getInlineKeyboard(
                    List.of("Строительство", "Инженерные сети", "Автоматика и система управления"),
                    List.of(1, 1, 1),
                    List.of(CONSTRUCTION, ENGINEERING, AUTOMATIZATION));
        }
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(chatId, commonInfo.getMessageId());
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                chatId,
                TextConstants.START_MESSAGE,
                inlineKeyboard
                );

        Photo photo = photoService.getPhotoByPhotoName("logo").orElse(null);
        log.info(photo.toString());
        if(photo != null){
            SendPhoto sendPhoto = AnswerMethodFactory.getSendPhoto(chatId, photo.getPhotoId());
            try {
                client.executeAsync(deleteMessage);
                client.execute(sendPhoto);
                client.execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                client.executeAsync(deleteMessage);
                client.execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
            }
        }
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Photo;
import ru.prusov.TelegramBotConstructionHelper.model.entity.User;
import ru.prusov.TelegramBotConstructionHelper.usecase.role.Role;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.PhotoService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.UserService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.START_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.role.Role.ADMIN;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCallbackCommand extends AbstractCallbackCommand {
    private final UserService userService;
    private final PhotoService photoService;
    private final StateService stateService;


    @Override
    public String command() {
        return START;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        User user = userService.findOrCreateUser(chatId, commonInfo.getUserFromTelegram().getFirstName());
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.NONE);

        deleteAllMessage(chatId);

        Optional<Photo> logo = photoService.getPhotoByPhotoName("logo");

        if (logo.isPresent()) {
            Photo photo = logo.get();
            sendPhotoAndTrack(chatId, photo.getPhotoId(), commonInfo.getMessageId() + 1);
        }

        replyAndTrack(chatId, START_MESSAGE, buildInlineKeyboardWithRoleOfUser(user.getRole()), commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    private InlineKeyboardMarkup buildInlineKeyboardWithRoleOfUser(Role role) {
        if (ADMIN.equals(role)) {
            return KeyboardFactory.getInlineKeyboard(
                    List.of("Строительство", "Инженерные сети", "Автоматика и система управления", "Настройки"),
                    List.of(1, 1, 1, 1),
                    List.of(CONSTRUCTION, ENGINEERING, AUTOMATIZATION, SETTINGS)
            );
        }
        return KeyboardFactory.getInlineKeyboard(
                List.of("Строительство", "Инженерные сети", "Автоматика и система управления"),
                List.of(1, 1, 1),
                List.of(CONSTRUCTION, ENGINEERING, AUTOMATIZATION)
        );
    }

}

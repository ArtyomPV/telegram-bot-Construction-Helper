package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Photo;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.PhotoService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.LOAD_LOGO;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadLogoImageState implements State {
    private final TelegramClient client;
    private final StateService stateService;
    private final PhotoService photoService;

    @Override
    public UserState state() {
        return LOAD_LOGO;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        log.info("Изображение сохранено");
        Long chatId = commonInfo.getChatId();
        stateService.clearUserStateByChatId(chatId);
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(chatId, commonInfo.getMessageId()-1);
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId,
                "Изображение установлено",
                KeyboardFactory.getInlineKeyboard(
                        List.of("Вернуться в главное меню"),
                        List.of(1),
                        List.of(START)
                )
        );
        try {
            client.execute(deleteMessage);
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}

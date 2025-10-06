package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.LOAD_LOGO;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadLogoImageState extends AbstractState {
    private final StateService stateService;

    @Override
    public UserState state() {
        return LOAD_LOGO;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.clearUserStateByChatId(chatId);

        deleteAllMessage(chatId);
        replyAndTrack(chatId,
                "Изображение установлено",
                KeyboardFactory.getInlineKeyboard(
                        List.of("Вернуться в главное меню"),
                        List.of(1),
                        List.of(START)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

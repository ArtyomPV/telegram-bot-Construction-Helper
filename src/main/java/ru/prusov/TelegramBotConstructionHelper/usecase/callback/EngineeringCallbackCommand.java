package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ENGINEERING;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class EngineeringCallbackCommand extends AbstractCallbackCommand {

    private final String CONTENT = "Данный раздел находится в разработке";
    private final String BACK_CONTENT = "⬅\uFE0F Назад";

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                CONTENT,
                KeyboardFactory.getInlineKeyboard(
                        List.of(BACK_CONTENT),
                        List.of(1),
                        List.of(START)),
                commonInfo.getMessageId() + 1
        );
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return ENGINEERING;
    }
}

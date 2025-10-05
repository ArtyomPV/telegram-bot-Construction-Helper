package ru.prusov.TelegramBotConstructionHelper.usecase.callback.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.LOAD_LOGO_MESSAGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.LOGO_CHANGE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.SETTINGS;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoChangeCallbackCommand extends AbstractCallbackCommand {

    private final StateService stateService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.setUserStateByChatId(chatId, UserState.LOAD_LOGO);

        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                LOAD_LOGO_MESSAGE,
                                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(SETTINGS)),
                commonInfo.getMessageId() + 1
                );

    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return LOGO_CHANGE;
    }


//    public String command() {
//        return LOGO_CHANGE;
//    }
//
//    @Override
//    public void execute(CommonInfo commonInfo) {
//        Long chatId = commonInfo.getChatId();
//
//        stateService.setUserStateByChatId(chatId, UserState.LOAD_LOGO);
//
//        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
//                chatId,
//                commonInfo.getMessageId(),
//                LOAD_LOGO_MESSAGE,
//                KeyboardFactory.getInlineKeyboard(
//                        List.of("Назад"),
//                        List.of(1),
//                        List.of(SETTINGS)
//                )
//        );
//        try {
//            client.execute(editMessageText);
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

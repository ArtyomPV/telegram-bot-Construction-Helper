package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_TITLE_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ADD_CONSTRUCTION_ITEM;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddConstructionItemCallbackCommand extends AbstractCallbackCommand {

    private final StateService stateService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.setUserStateByChatId(chatId, UserState.WAITING_CONSTRUCTION_ITEM_TITLE);

        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                ASK_TITLE_CONSTRUCTION_ITEM,
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return ADD_CONSTRUCTION_ITEM;
    }

//    @Override
//    public String command() {
//        return ADD_CONSTRUCTION_ITEM;
//    }
//
//    @Override
//    public void execute(CommonInfo commonInfo) {
//        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_CONSTRUCTION_ITEM_TITLE);
//        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
//                commonInfo.getChatId(),
//                commonInfo.getMessageId(),
//                ASK_TITLE_CONSTRUCTION_ITEM
//        );
//
//        try {
//            client.execute(editMessageText);
//        } catch (TelegramApiException e) {
//            log.error("Request failed: object name - class {}",
//                    AddConstructionItemCallbackCommand.class.getSimpleName());
//        }
//    }
}

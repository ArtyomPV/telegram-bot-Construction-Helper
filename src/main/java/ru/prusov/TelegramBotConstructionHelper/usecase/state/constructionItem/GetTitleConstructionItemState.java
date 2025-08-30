package ru.prusov.TelegramBotConstructionHelper.usecase.state.constructionItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.dto.KeeperId;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_DESCRIPTION_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_CONSTRUCTION_ITEM_TITLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTitleConstructionItemState implements State {
    private final KeeperId keeperId;
    private final TelegramClient client;
    private final ConstructionItemService constructionItemService;
    private final StateService stateService;

    @Override
    public UserState state() {
        return WAITING_CONSTRUCTION_ITEM_TITLE;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        ConstructionItem constructionItem = new ConstructionItem();
        constructionItem.setTitle(commonInfo.getMessageText());
        Long idConstructionItem = constructionItemService.save(constructionItem);
        keeperId.setId(idConstructionItem);
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_CONSTRUCTION_ITEM_DESCRIPTION);
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(commonInfo.getChatId(), commonInfo.getMessageId());
        EditMessageText sendMessage = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 1,
                ASK_DESCRIPTION_CONSTRUCTION_ITEM);

        try {
            client.execute(deleteMessage);
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error executing method in class {}", GetTitleConstructionItemState.class.getSimpleName());
        }
    }
}

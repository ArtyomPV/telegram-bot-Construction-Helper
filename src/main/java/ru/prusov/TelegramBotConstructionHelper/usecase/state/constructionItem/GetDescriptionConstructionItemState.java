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

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_PHOTO_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_CONSTRUCTION_ITEM_DESCRIPTION;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDescriptionConstructionItemState implements State {
    private final TelegramClient client;
    private final KeeperId keeperId;
    private final StateService stateService;
    private final ConstructionItemService constructionItemService;

    @Override
    public UserState state() {
        return WAITING_CONSTRUCTION_ITEM_DESCRIPTION;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        Long constructionItemId = keeperId.getId();
        ConstructionItem constructionItem = constructionItemService.getConstructionItemById(constructionItemId).get();
        constructionItem.setDescription(commonInfo.getMessageText());
        constructionItemService.save(constructionItem);
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_CONSTRUCTION_ITEM_PHOTO);
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId()
        );
        EditMessageText sendMessage = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 2,
                ASK_PHOTO_CONSTRUCTION_ITEM
        );

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error executing edit method in class {}", GetDescriptionConstructionItemState.class.getSimpleName());
        }
        try {
            client.execute(deleteMessage);
        } catch (TelegramApiException e) {
            log.error("Error executing delete method in class {}", GetDescriptionConstructionItemState.class.getSimpleName());
        }

    }
}

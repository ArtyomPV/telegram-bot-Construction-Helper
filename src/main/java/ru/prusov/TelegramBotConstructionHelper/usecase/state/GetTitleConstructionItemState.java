package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.dto.KeeperId;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_DESCRIPTION_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_TITLE_CONSTRUCTION_ITEM;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTitleConstructionItemState implements State{
    private final KeeperId keeperId;
    private final TelegramClient client;
    private final ConstructionItemService constructionItemService;
    private final StateService stateService;
    @Override
    public UserState state() {
        return WAITING_TITLE_CONSTRUCTION_ITEM;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        ConstructionItem constructionItem = new ConstructionItem();
        constructionItem.setTitle(commonInfo.getMessageText());
        Long idConstructionItem = constructionItemService.save(constructionItem);
        keeperId.setId(idConstructionItem);
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_DESCRIPTION_CONSTRUCTION_ITEM);
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                ASK_DESCRIPTION_CONSTRUCTION_ITEM);

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error executing method in class {}", GetTitleConstructionItemState.class.getSimpleName());
        }
    }
}

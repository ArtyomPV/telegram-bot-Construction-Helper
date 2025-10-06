package ru.prusov.TelegramBotConstructionHelper.usecase.state.constructionItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.dto.KeeperId;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_DESCRIPTION_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_CONSTRUCTION_ITEM_TITLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTitleConstructionItemState extends AbstractState {
    private final KeeperId keeperId;
    private final ConstructionItemService constructionItemService;
    private final StateService stateService;

    @Override
    public UserState state() {
        return WAITING_CONSTRUCTION_ITEM_TITLE;
    }


    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();

        ConstructionItem constructionItem = new ConstructionItem();
        constructionItem.setTitle(commonInfo.getMessageText());
        Long idConstructionItem = constructionItemService.save(constructionItem);
        keeperId.setId(idConstructionItem);
        stateService.setUserStateByChatId(chatId, UserState.WAITING_CONSTRUCTION_ITEM_DESCRIPTION);

        replyAndTrack(
                chatId,
                ASK_DESCRIPTION_CONSTRUCTION_ITEM,
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

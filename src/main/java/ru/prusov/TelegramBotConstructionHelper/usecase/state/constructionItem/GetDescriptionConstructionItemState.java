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

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.ASK_PHOTO_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_CONSTRUCTION_ITEM_DESCRIPTION;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDescriptionConstructionItemState extends AbstractState {

    private final KeeperId keeperId;
    private final StateService stateService;
    private final ConstructionItemService constructionItemService;

    @Override
    public UserState state() {
        return WAITING_CONSTRUCTION_ITEM_DESCRIPTION;
    }


    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long constructionItemId = keeperId.getId();
        ConstructionItem constructionItem = constructionItemService.getConstructionItemById(constructionItemId).get();
        constructionItem.setDescription(commonInfo.getMessageText());

        constructionItemService.save(constructionItem);
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.WAITING_CONSTRUCTION_ITEM_PHOTO);

        replyAndTrack(commonInfo.getChatId(),
                ASK_PHOTO_CONSTRUCTION_ITEM,
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log ;
    }
}

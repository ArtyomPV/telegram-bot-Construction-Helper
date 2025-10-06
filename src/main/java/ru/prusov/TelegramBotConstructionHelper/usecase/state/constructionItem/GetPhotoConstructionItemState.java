package ru.prusov.TelegramBotConstructionHelper.usecase.state.constructionItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.dto.KeeperId;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ADD_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_CONSTRUCTION_ITEM_PHOTO;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetPhotoConstructionItemState extends AbstractState {
    private final StateService stateService;
    private final ConstructionItemService constructionItemService;
    private final KeeperId keeperId;

    @Override
    public UserState state() {
        return WAITING_CONSTRUCTION_ITEM_PHOTO;
    }



    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Long constructionItemId = keeperId.getId();
        ConstructionItem constructionItem = constructionItemService.getConstructionItemById(constructionItemId).get();
        constructionItem.setPhotoFileId(commonInfo.getFileId());
        constructionItemService.save(constructionItem);
        stateService.setUserStateByChatId(commonInfo.getChatId(), UserState.NONE);

        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                "Объект сохранен.",
                KeyboardFactory.getInlineKeyboard(
                        List.of("Добавить следующий объект", "Назад"),
                        List.of(1, 1),
                        List.of(ADD_CONSTRUCTION_ITEM, START)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.dto.KeeperId;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PHOTO_CONSTRUCTION_ITEM;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetPhotoConstructionItemState implements State{
    private final TelegramClient client;
    private final StateService stateService;
    private final ConstructionItemService constructionItemService;
    private final KeeperId keeperId;
    @Override
    public UserState state() {
        return WAITING_PHOTO_CONSTRUCTION_ITEM;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {

    }
}

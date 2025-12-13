package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.UPDATE_NEW_OPTION_CONTRACT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_UPDATE_OPTIONS_CONTRACT;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetUpdateContractOptionCallback extends AbstractCallbackCommand {
    private String CONTENT_TEXT = """
    Выберите пункт который хотите изменить:
    
    🖋  Описание договора
    🖋  Начало работ
    🖋  Завершение работ
    🖋  Стоимость договора
    """;
    private final StateService stateService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();

        stateService.setUserStateByChatId(chatId, WAITING_UPDATE_OPTIONS_CONTRACT);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return UPDATE_NEW_OPTION_CONTRACT;
    }
}



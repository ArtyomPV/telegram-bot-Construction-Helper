package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_CREATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateContractCallbackCommand extends AbstractCallbackCommand {

    private final StateService stateService;
    private final String CONTENT_TEXT = "Введите номер договора.";

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.setUserStateByChatId(chatId, UserState.CONTRACT_NUMBER);

        deleteAllMessage(chatId);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return CONTRACTS_CREATE;
    }
}

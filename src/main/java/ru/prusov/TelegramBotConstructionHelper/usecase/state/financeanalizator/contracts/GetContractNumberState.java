package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_DESCRIPTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_NUMBER;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractNumberState extends AbstractState {

    private final StateService stateService;
    private final ContractDTOFull contractDTO;
    private final String CONTENT_TEXT = "Введите описание договора: ";

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        contractDTO.setContractNumber(commonInfo.getMessageText());

        stateService.setUserStateByChatId(chatId, CONTRACT_DESCRIPTION);
        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return CONTRACT_NUMBER;
    }
}

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

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractDescriptionState extends AbstractState {

    private final StateService stateService;
    private final ContractDTOFull contractDTO;
    private final String CONTENT_TEXT = "Введите наименование заказчика: ";

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        contractDTO.setDescription(commonInfo.getMessageText());
        stateService.setUserStateByChatId(chatId, UserState.CONTRACT_CUSTOMER);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);

    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return UserState.CONTRACT_DESCRIPTION;
    }
}

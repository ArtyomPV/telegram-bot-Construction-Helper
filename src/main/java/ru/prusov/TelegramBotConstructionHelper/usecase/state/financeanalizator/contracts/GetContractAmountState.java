package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.math.BigDecimal;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractAmountState extends AbstractState {

    private final StateService stateService;
    private final ContractDTOFull contractDTO;
    private final String CONTENT_TEXT = """
            Введите начало действия договора: 
            
            (Формат ввода корректной даты: ГГГГ-ММ-ДД)
            """;
    private final String CONTENT_TEXT_WRONG_AMOUNT = """
    ❗️❗️❗️ НЕ ВЕРНО ВВЕДЕНА СТОИМОСТЬ ДОГОВОРА, ❗️❗️❗️
                    ПОВТОРИТЕ ПОПЫТКУ
    """;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();

        try {
            double amountDouble = Double.parseDouble(messageText);
            BigDecimal contractAmount = new BigDecimal(amountDouble);
            contractDTO.setContractAmount(contractAmount);
            stateService.setUserStateByChatId(chatId, UserState.CONTRACT_START);
            replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
        } catch (NumberFormatException e) {
            replyAndTrack(chatId, CONTENT_TEXT_WRONG_AMOUNT, commonInfo.getMessageId() + 1);
        }
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return UserState.CONTRACT_AMOUNT;
    }
}

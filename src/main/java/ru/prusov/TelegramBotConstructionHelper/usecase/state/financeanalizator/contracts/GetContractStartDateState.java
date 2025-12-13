package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.common.DateValidator;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_END;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_START;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractStartDateState extends AbstractState {

    private final StateService stateService;
    private final ContractDTOFull contractDTO;

    private final String CONTENT_TEXT = """
            Введите окончание действия договора: 
            
            (Формат ввода корректной даты: ГГГГ-ММ-ДД)
            """;
    private final String CONTENT_TEXT_WRONG_DATE = """
            ❗️❗️❗️Не верный формат даты ❗️❗️❗️
                    Введите дату в формате ГГГГ-ММ-ДД
            """;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText().trim();

        if (!DateValidator.isValidDate(messageText)) {
            replyAndTrack(chatId, CONTENT_TEXT_WRONG_DATE, commonInfo.getMessageId() + 1);
        }

        LocalDate startDate = LocalDate.parse(messageText, DateTimeFormatter.ISO_LOCAL_DATE);
        contractDTO.setStartDate(startDate);

        stateService.setUserStateByChatId(chatId, CONTRACT_END);
        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);

    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return CONTRACT_START;
    }
}

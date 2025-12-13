package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.common.DateValidator;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_END;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractEndDateState extends AbstractState {

    private final StateService stateService;
    private final ContractService contractService;
    private final ContractDTOFull contractDTO;

    private final String CONTENT_TEXT = """
            Договор сохранен! ✅ 
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

        LocalDate endData = LocalDate.parse(messageText, DateTimeFormatter.ISO_LOCAL_DATE);
        contractDTO.setEndDate(endData);

        stateService.setUserStateByChatId(chatId, CONTRACT_END);

        Contract contract = contractService.convertToContract(contractDTO);
        contractService.saveContract(contract);
        stateService.clearUserStateByChatId(chatId);


        replyAndTrack(chatId, CONTENT_TEXT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Добавить следующий договор", "Назад в главное меню"),
                        List.of(1, 1),
                        List.of(CONTRACTS_CREATE, START)),
                commonInfo.getMessageId() + 1);



    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return CONTRACT_END;
    }


}

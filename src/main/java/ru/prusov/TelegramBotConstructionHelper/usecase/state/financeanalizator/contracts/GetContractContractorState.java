package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contractor;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractorService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_AMOUNT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_CONTRACTOR;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractContractorState extends AbstractState {

    private final StateService stateService;
    private final ContractDTOFull contractDTO;
    private final ContractorService contractorService;
    private final String CONTENT_TEXT = "Введите стоимость контракта: ";
    private final String CONTENT_TEXT_NO_CORRECT = """
            Введенное наименование отсутствует в базе данных,
            сохраните исполнителя или 
            введите другое наименование исполнителя: 
            """;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String name = commonInfo.getMessageText();

        Optional<Contractor> customerByName = contractorService.getContractorByName(name);

        customerByName.ifPresentOrElse(
                contractor -> {
                    contractDTO.setContractor(contractor);
                    stateService.setUserStateByChatId(chatId, CONTRACT_AMOUNT);
                    replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
                },
                () -> {
                    replyAndTrack(chatId, CONTENT_TEXT_NO_CORRECT, getSaveContractorKeyboard(), commonInfo.getMessageId() + 1);
                });
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return UserState.CONTRACT_CONTRACTOR;
    }

    private InlineKeyboardMarkup getSaveContractorKeyboard() {
        return KeyboardFactory.getInlineKeyboard(
                List.of("Сохранить исполнителя", "Вернуться в главное меню"),
                List.of(1, 1),
                List.of(CONTRACTOR_SAVE, START)
        );
    }
}

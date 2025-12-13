package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_UPDATE;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaveUpdatedContractCallback extends AbstractCallbackCommand {

    private final String CONTENT_TEXT = " Изменения успешно сохранены ";
    private final String CONTENT_TEXT_NOT_SAVED = "Данные не сохранены. Проверьте данные ";

    private final StateService stateService;
    private final ContractService contractService;
    private final ContractDTOFull contractDTOFull;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        boolean isSaved = contractService.updateContract(contractDTOFull);
        if (!isSaved) {
            stateService.setUserStateByChatId(chatId, CONTRACT_UPDATE);
            replyAndTrack(chatId, CONTENT_TEXT_NOT_SAVED, commonInfo.getMessageId() + 1);
        }

        stateService.clearUserStateByChatId(chatId);
        replyAndTrack(chatId,
                CONTENT_TEXT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Просмотр договоров", "В главное меню"),
                        List.of(1, 1),
                        List.of(CONTRACTS_CONTRACT, START)
                ),
                commonInfo.getMessageId() + 1
        );
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return SAVE_UPDATED_CONTRACT;
    }
}

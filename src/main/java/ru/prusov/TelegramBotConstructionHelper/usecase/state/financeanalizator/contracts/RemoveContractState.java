package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_CONTRACT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_DELETE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_REMOVE;

@Component
@Slf4j
@RequiredArgsConstructor
public class RemoveContractState extends AbstractState {

    private final StateService stateService;
    private final ContractService contractService;

    private final String CONTENT_TEXT = "Договор удален";
    private final String CONTENT_TEXT_WRONG_NUMBER = "Договора с таким номером нет";

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        try {
            stateService.clearUserStateByChatId(chatId);
            contractService.deleteByContractNumber(messageText);
            replyAndTrack(chatId,
                    CONTENT_TEXT,
                    KeyboardFactory.getInlineKeyboard(
                            List.of("Удалить еще договор?", "Назад"),
                            List.of(1, 1),
                            List.of(CONTRACTS_DELETE, CONTRACTS_CONTRACT)
                    ),
                    commonInfo.getMessageId() + 1);
        } catch (Exception e) {
            replyAndTrack(chatId, CONTENT_TEXT_WRONG_NUMBER, commonInfo.getMessageId() + 1);
        }


    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return CONTRACT_REMOVE;
    }

}

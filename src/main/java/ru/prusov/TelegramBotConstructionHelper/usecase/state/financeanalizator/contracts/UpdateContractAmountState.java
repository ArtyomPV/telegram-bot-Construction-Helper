package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.math.BigDecimal;
import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.SAVE_UPDATED_CONTRACT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.UPDATE_NEW_OPTION_CONTRACT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_UPDATE_CONTRACT_AMOUNT;


@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateContractAmountState extends AbstractState {
    private String CONTENT_TEXT = """
            Изменена стоимость контракта
            
            Выберите продолжить изменение или сохранить изменения""";

    private final StateService stateService;
    private  final ContractDTOFull contractDTOFull;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        contractDTOFull.setContractAmount(new BigDecimal(messageText));

        replyAndTrack(chatId,
                CONTENT_TEXT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Изменить следующий пункт", "Завершить изменение проекта"),
                        List.of(1 ,1),
                        List.of(UPDATE_NEW_OPTION_CONTRACT, SAVE_UPDATED_CONTRACT)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return WAITING_UPDATE_CONTRACT_AMOUNT;
    }
}

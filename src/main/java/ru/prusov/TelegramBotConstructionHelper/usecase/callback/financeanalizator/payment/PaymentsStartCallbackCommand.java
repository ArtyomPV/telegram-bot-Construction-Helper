package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.PaymentService;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_PAYMENTS;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_CONTRACT_NUMBER;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentsStartCallbackCommand extends AbstractCallbackCommand {

    private final String CONTENT_TEXT = """
            Выберите номер договора
            
            """;

    private final StateService stateService;
    private final PaymentService paymentService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        stateService.setUserStateByChatId(chatId, WAITING_PAYMENT_CONTRACT_NUMBER);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);

    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return CONTRACTS_PAYMENTS;
    }
}

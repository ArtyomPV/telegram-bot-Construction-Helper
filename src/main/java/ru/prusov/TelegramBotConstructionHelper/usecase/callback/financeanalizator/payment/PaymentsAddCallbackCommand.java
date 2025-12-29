package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDtoComponent;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.PaymentService;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.PAYMENT_ADD;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_AMOUNT;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentsAddCallbackCommand extends AbstractCallbackCommand {
    private final String CONTENT_TEXT = """
            Введите сумму платежа ⬇️ 
            """;
    private final StateService stateService;
    private final PaymentDtoComponent paymentDtoComponent;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String[] split = commonInfo.getMessageText().split(":");
        Long contractId = Long.parseLong(split[1]);
        paymentDtoComponent.setContractId(contractId);
        stateService.setUserStateByChatId(chatId, WAITING_PAYMENT_AMOUNT);

        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return PAYMENT_ADD;
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDtoComponent;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;
import java.math.BigDecimal;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_AMOUNT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentAddAmountStateCommand extends AbstractState {

    private final String CONTENT_TEXT = """
            Введите назначение платежа:
                Аванс
                Оплата
                Штраф/неустойка
                Премия/бонус
            """;
    private final String CONTENT_TEXT_WRONG_AMOUNT = """
            ❌ Введите корректную сумму ❌
            """;
    private final StateService stateService;
    private final PaymentDtoComponent paymentDtoComponent;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        try {
            BigDecimal amount = getAmount(messageText);
        } catch (IllegalArgumentException e){
            replyAndTrack(chatId, CONTENT_TEXT_WRONG_AMOUNT, commonInfo.getMessageId() + 1);
            return;
        }

        stateService.setUserStateByChatId(chatId, WAITING_PAYMENT_TYPE);
        replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
    }
    private BigDecimal getAmount(String messageText){
        try {
            BigDecimal amount = new BigDecimal(messageText.trim());
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException();
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return WAITING_PAYMENT_AMOUNT;
    }
}

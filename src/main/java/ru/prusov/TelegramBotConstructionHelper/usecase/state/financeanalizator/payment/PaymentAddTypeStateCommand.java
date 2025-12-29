package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDtoComponent;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.PaymentType;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.PaymentService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_DATE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_TYPE;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentAddTypeStateCommand extends AbstractState {

    private final String CONTENT_TEXT = """
            Введите дату проведения платежа
            
            Для установки текущей даты отправьте пустое сообщение
            """;
    private final String CONTENT_TEXT_WRONG_AMOUNT = """
            ❌ Введите корректное назначение платежа ❌
            """;

    private final StateService stateService;
    private final PaymentDtoComponent paymentDtoComponent;
    private final PaymentService paymentService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        try {
            PaymentType paymentType = paymentService.convertTextToPaymentType(messageText);
            paymentDtoComponent.setPaymentType(paymentType);
            stateService.setUserStateByChatId(chatId, WAITING_PAYMENT_DATE);
            replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
        } catch (IllegalArgumentException e) {
            replyAndTrack(chatId, CONTENT_TEXT_WRONG_AMOUNT, commonInfo.getMessageId() + 1);
        }

    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return WAITING_PAYMENT_TYPE;
    }
}

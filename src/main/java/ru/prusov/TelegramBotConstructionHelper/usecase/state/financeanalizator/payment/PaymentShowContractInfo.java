package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.PaymentDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.PaymentService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_PAYMENT_CONTRACT_NUMBER;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentShowContractInfo extends AbstractState {
    private final String CONTENT_TEXT = "Договор с номером ";
    private final String CONTENT_TEXT_NO_CONTRACT = """
            ❌ Договор с данным номером отсутствует  ❌
            Введите корректный номер договора.
            """;

    private final StateService stateService;
    private final ContractService contractService;
    private final PaymentService paymentService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {

        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();

        Optional<Contract> optionalContract = contractService.getContractByContractNumber(messageText);

        optionalContract.ifPresentOrElse(contract -> {
            List<PaymentDTO> paymentsByContractId = paymentService.findPaymentsByContractId(contract.getId());
            BigDecimal sumAllPaymentsByContract = getSumAllPaymentsByContract(paymentsByContractId);
            String contentText = getContentText(paymentsByContractId, contract, sumAllPaymentsByContract);

            replyAndTrack(chatId,
                    contentText,
                    getInlineKeyboard(),
                    commonInfo.getMessageId() + 1);
        }, () -> {
            replyAndTrack(chatId, CONTENT_TEXT_NO_CONTRACT, commonInfo.getMessageId() + 1);
        });
    }
    private InlineKeyboardMarkup getInlineKeyboard(){
        return KeyboardFactory.getInlineKeyboard(
                List.of("Добавить запись", "Изменить запись", "Удалить запись", "В меню договоров"),
                List.of(1, 1, 1, 1),
                List.of(PAYMENT_ADD, PAYMENT_UPDATE, PAYMENT_REMOVE, CONTRACTS)
        );
    }

    private String getContentText(List<PaymentDTO> paymentsByContractId,
                                  Contract contract,
                                  BigDecimal sumAllPaymentsByContract) {

        StringBuilder content = new StringBuilder();
        content.append("Договор с номером ").append(contract.getContractNumber()).append("\n");
        content.append("    Содержание договора: ").append(contract.getDescription()).append("\n");
        content.append("    Стоимость договора: ").append(contract.getContractAmount()).append("\n");
        content.append("    Начало действия договора: ").append(contract.getStartDate()).append("\n");
        content.append("    Окончание действия договора: ").append(contract.getEndDate()).append("\n");
        content.append("    Статус договора: ")
                .append(contract.getIsCompleted() ? "Закрыт ✅" : "В исполнении ❌").append("\n");
        content.append("\n\n");
        if (paymentsByContractId.isEmpty()) {
            content.append("Авансовых выплат не было");
        } else {
            content.append("💳 Произведенные выплаты\n");
            for (PaymentDTO payment : paymentsByContractId) {
                content.append("✅  ").append(payment.getPaymentDate()).append(": ").append(payment.getAmount()).append("\n");
            }

            content.append("\nВсего выплат по договору:  ").append(sumAllPaymentsByContract);
        }
        return content.toString();
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return WAITING_PAYMENT_CONTRACT_NUMBER;
    }


    private BigDecimal getSumAllPaymentsByContract(List<PaymentDTO> paymentsByContractId) {
        return paymentsByContractId.stream()
                .map(PaymentDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

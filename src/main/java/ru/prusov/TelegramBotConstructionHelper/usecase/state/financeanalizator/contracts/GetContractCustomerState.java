package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Customer;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.CustomerService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CUSTOMER_SAVE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_CONTRACTOR;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.CONTRACT_CUSTOMER;

@Component
@Slf4j
@RequiredArgsConstructor
public class GetContractCustomerState extends AbstractState {
    private final StateService stateService;
    private final CustomerService customerService;
    private final ContractDTOFull contractDTO;

    private final String CONTENT_TEXT = "Введите наименование исполнителя: ";
    private final String CONTENT_TEXT_NO_CORRECT = """
            Введенное наименование отсутствует в базе данных,
            сохраните заказчика или 
            введите другое наименование заказчика: 
            """;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String name = commonInfo.getMessageText();

        Optional<Customer> customerByName = customerService.getCustomerByName(name);

        customerByName.ifPresentOrElse(
                customer -> {
                    contractDTO.setCustomer(customer);
                    stateService.setUserStateByChatId(chatId, CONTRACT_CONTRACTOR);
                    replyAndTrack(chatId, CONTENT_TEXT, commonInfo.getMessageId() + 1);
                },
                ()-> {
                    replyAndTrack(chatId, CONTENT_TEXT_NO_CORRECT, getSaveCustomerKeyboard(), commonInfo.getMessageId() + 1);
                });
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return CONTRACT_CUSTOMER;
    }

    private InlineKeyboardMarkup getSaveCustomerKeyboard(){
        return KeyboardFactory.getInlineKeyboard(
                List.of("Сохранить заказчика", "Вернуться в главное меню"),
                List.of(1, 1),
                List.of(CUSTOMER_SAVE, START)
              );
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.callback.financeanalizator.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ContractStartCallbackCommand extends AbstractCallbackCommand {

    private final String CONTENT = """
            Данный раздел предназначен для работы с договорами 📄: 
            1️⃣   регистрация ✍️ и редактирование договорных документов ✏️, 
            2️⃣   учёт и фиксация поступлений денежных средств 💳 по договорам. 
            
            3️⃣   Предусмотрена возможность добавления и сохранения данных контрагента 👥, 
            4️⃣   просмотра истории платежей 📜 и формирования списка активных договоров 📋.
            """;


    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();

        deleteAllMessage(chatId);

        replyAndTrack(chatId,
                CONTENT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Договора", "Платежи", "Контрагенты", "Назад"),
                        List.of(1, 1, 1, 1),
                        List.of(CONTRACTS_CONTRACT, CONTRACTS_PAYMENTS, CONTRACTS_CONTRACTOR, START)),
                commonInfo.getMessageId() + 1);


    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return CONTRACTS;
    }
}

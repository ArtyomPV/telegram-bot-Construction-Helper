package ru.prusov.TelegramBotConstructionHelper.usecase.state.financeanalizator.contracts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTOFull;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contract;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_UPDATE_OPTIONS_CONTRACT;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateContractState extends AbstractState {

    private final String CONTENT_HEADER = "Выбран договор: \n";
    private final StateService stateService;
    private final ContractService contractService;
    private ContractDTOFull contractDTOFull;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        Optional<Contract> contractOptional =  contractService.getContractByContractNumber(messageText);
        StringBuilder content = new StringBuilder();
        contractOptional.ifPresentOrElse(

                contract -> {
                    content.append(CONTENT_HEADER);
                    content.append(contract.getContractNumber()).append(" :\n ");
                    content.append("   Описание договора: ").append(contract.getDescription()).append("\n");
                    content.append("   Начало работ: ").append(contract.getStartDate()).append("\n");
                    content.append("   Завершение работ: ").append(contract.getEndDate()).append("\n");
                    content.append("   Стоимость договора: ").append(contract.getContractAmount()).append("\n");
                    content.append("   Статус договора: ")
                            .append(contract.getIsCompleted() ? "Закрыт ✅" : "В исполнении ❌")
                            .append("\n");
                    content.append("Выберите пункт который хотите изменить \n\n")
                            .append("❓ Напишите пункт который нужно изменить ❓");

                    contractDTOFull = contractService.convertToDtoFull(contract);

                    stateService.setUserStateByChatId(chatId, WAITING_UPDATE_OPTIONS_CONTRACT);
                },
                () -> {

        content.append("Договор не найден");
                }
        );

        replyAndTrack(chatId, content.toString(), commonInfo.getMessageId());
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public UserState state() {
        return UserState.CONTRACT_UPDATE;
    }
}

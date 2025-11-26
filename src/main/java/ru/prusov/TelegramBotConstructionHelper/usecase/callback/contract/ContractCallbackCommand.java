package ru.prusov.TelegramBotConstructionHelper.usecase.callback.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_CONTRACT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractCallbackCommand extends AbstractCallbackCommand {
    private final ContractService contractService;

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        int position = 0;
        String header = "";
        StringBuilder content = new StringBuilder();
        List<ContractDTO> contractDTOList = contractService.findAll();
        if(contractDTOList.isEmpty()){
            content.append("Сохраненных договоров нет");
        } else {
            for(ContractDTO contractDTO: contractDTOList){
                content.append(++position).append(". ");
               content.append(contractDTO.getContractNumber()).append(" - ");
               content.append(contractDTO.getDescription()).append("\n");
               content.append("   Начало работ: ").append(contractDTO.getStartDate()).append("\n");
               content.append("   Завершение работ: ").append(contractDTO.getEndDate()).append("\n");
               content.append("   Стоимость договора: ").append(contractDTO.getContractAmount()).append("\n");
            }
        }

        replyAndTrack(chatId,
                content.toString(),
                KeyboardFactory.getInlineKeyboard(
                      List.of("Назад"),
                      List.of(1),
                      List.of(START)
                ),
                commonInfo.getMessageId() + 1 );



    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return CONTRACTS_CONTRACT;
    }
}

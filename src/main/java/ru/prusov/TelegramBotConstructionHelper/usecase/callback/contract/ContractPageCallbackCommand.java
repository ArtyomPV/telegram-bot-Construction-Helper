package ru.prusov.TelegramBotConstructionHelper.usecase.callback.contract;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.dto.ContractDTO;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.service.ContractService;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;

import java.util.ArrayList;
import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.common.CommonVariable.contractPageSize;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ContractPageCallbackCommand extends AbstractCallbackCommand {
    private final ContractService contractService;
    private final String BUTTON_PREVIOUS = "⬅\uFE0F Предыдущий список";
    private final String BUTTON_NEXT = "Следующий список ➡\uFE0F";
    private final String BUTTON_BACK = "Назад";



    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();

        int currentPageNumber = extractCurrentPage(messageText);
        int position = currentPageNumber * contractPageSize;
        Pageable pageable = PageRequest.of(currentPageNumber, contractPageSize, Sort.by("startDate").descending());
        Page<ContractDTO> contractDTOList = contractService.findAll(pageable);

        StringBuilder contentText = new StringBuilder();
        if(contractDTOList.isEmpty()){
            contentText.append("\uD83D\uDDD2 Договора закончились");
        } else {
            for(ContractDTO contractDTO: contractDTOList){
                contentText.append(++position).append(". ");
                contentText.append(contractDTO.getContractNumber()).append(" - ");
                contentText.append(contractDTO.getDescription()).append("\n");
                contentText.append("   Начало работ: ").append(contractDTO.getStartDate()).append("\n");
                contentText.append("   Завершение работ: ").append(contractDTO.getEndDate()).append("\n");
                contentText.append("   Стоимость договора: ").append(contractDTO.getContractAmount()).append("\n");
                contentText.append("   Статус договора: ")
                        .append(contractDTO.getIsCompleted() ? "Закрыт" : "В исполнении ")
                        .append("\n");

            }
        }

        replyAndTrack(chatId,
                contentText.toString(),
                getKeyboard(contractDTOList, currentPageNumber),
                commonInfo.getMessageId() + 1);
    }

    private InlineKeyboardMarkup getKeyboard(Page<ContractDTO> contractDTOList, int currentPageNumber){
        boolean hasPrevious = currentPageNumber > 0;
        boolean hasNext = contractDTOList.hasNext();

        List<String> buttonText = new ArrayList<>();
        List<Integer> buttonLayout = new ArrayList<>();
        List<String> buttonCallback = new ArrayList<>();

        if(hasPrevious){
            buttonText.add(BUTTON_PREVIOUS);
            buttonLayout.add(1);
            buttonCallback.add(CONTRACTS_PAGE + (currentPageNumber - 1));
        }

        if(hasNext){
            buttonText.add(BUTTON_NEXT);
            buttonLayout.add(1);
            buttonCallback.add(CONTRACTS_PAGE + (currentPageNumber + 1));
        }

        buttonText.add(BUTTON_BACK);
        buttonLayout.add(1);
        buttonCallback.add(CONTRACTS);
        return KeyboardFactory.getInlineKeyboard(
                buttonText,
                buttonLayout,
                buttonCallback
        );
    }

    private int extractCurrentPage(String messageText) {
        String[] splitText = messageText.split("_");
        try{
           return Integer.parseInt(splitText[2]);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    @Override
    protected Logger log() {
        return log;
    }

    @Override
    public String command() {
        return CONTRACTS_PAGE;
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemDtoService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreviousConstructionItemCallbackCommand extends AbstractCallbackCommand {

    private final ConstructionItemService constructionItemService;
    private final ConstructionItemDtoService constructionItemDtoService;

    @Override
    public String command() {
        return PREV_CONSTRUCTION;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Long idCurrentConstructionItem = constructionItemDtoService.getConstructionItemDto().getId();
        ConstructionItem currentConstructionItem = constructionItemService.getConstructionItemById(idCurrentConstructionItem).get();

        constructionItemService.getPrev(currentConstructionItem).ifPresentOrElse(
                prevConstructionItem -> {
                    showPrevConstructionItem(prevConstructionItem, chatId, commonInfo);
                    constructionItemDtoService.saveConstructionItemDto(prevConstructionItem);

                },
                () -> {
                    ConstructionItem constructionItem = constructionItemService.getLast().get();
                    showPrevConstructionItem(constructionItem, chatId, commonInfo);
                    constructionItemDtoService.saveConstructionItemDto(constructionItem);
                }
        );
    }

    @Override
    protected Logger log() {
        return log;
    }

    private void showPrevConstructionItem(ConstructionItem prevConstructionItem,
                                          Long chatId,
                                          CommonInfo commonInfo) {

        constructionItemDtoService.saveConstructionItemDto(prevConstructionItem);
        editReply(chatId, prevConstructionItem.getTitle(), commonInfo.getMessageId() - 2);
        editSendPhoto(chatId, prevConstructionItem.getPhotoFileId(), commonInfo.getMessageId() - 1);
        editReply(chatId,
                prevConstructionItem.getDescription(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Предыдущий", "Следующий", "Назад"),
                        List.of(2, 1),
                        List.of(PREV_CONSTRUCTION, NEXT_CONSTRUCTION, CONSTRUCTION)),
                commonInfo.getMessageId());
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemDtoService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreviousConstructionItemCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final ConstructionItemService constructionItemService;
    private final ConstructionItemDtoService constructionItemDtoService;

    @Override
    public String command() {
        return PREV_CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Long idCurrentConstructionItem = constructionItemDtoService.getConstructionItemDto().getId();
        ConstructionItem currentConstructionItem = constructionItemService.getConstructionItemById(idCurrentConstructionItem);

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

    private void showPrevConstructionItem(ConstructionItem prevConstructionItem,
                                          Long chatId,
                                          CommonInfo commonInfo) {

        constructionItemDtoService.saveConstructionItemDto(prevConstructionItem);

        EditMessageText titleText = AnswerMethodFactory.getEditMessageText(chatId,
                commonInfo.getMessageId() - 2,
                prevConstructionItem.getTitle());

        EditMessageMedia sendPhoto = AnswerMethodFactory.getEditMessageMedia(chatId,
                commonInfo.getMessageId() - 1,
                prevConstructionItem.getPhotoFileId());

        EditMessageText descriptionText = AnswerMethodFactory.getEditMessageText(chatId,
                commonInfo.getMessageId(),
                prevConstructionItem.getDescription(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Предыдущий", "Следующий", "Назад"),
                        List.of(2, 1),
                        List.of(PREV_CONSTRUCTION, NEXT_CONSTRUCTION, CONSTRUCTION)
                ));

        try {
            client.execute(titleText);
            client.execute(sendPhoto);
            client.execute(descriptionText);
        } catch (TelegramApiException e) {
            log.error("Request failed: object name - class {}", NextConstructionItemCallbackCommand.class.getSimpleName());
        }
    }
}

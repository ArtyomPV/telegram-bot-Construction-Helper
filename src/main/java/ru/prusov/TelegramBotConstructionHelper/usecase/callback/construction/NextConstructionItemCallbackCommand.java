package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
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
public class NextConstructionItemCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final ConstructionItemService constructionItemService;
    private final ConstructionItemDtoService constructionItemDtoService;

    @Override
    public String command() {
        return NEXT_CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        Long idCurrentConstructionItem = constructionItemDtoService.getConstructionItemDto().getId();
        ConstructionItem currentConstructionItem = constructionItemService.getConstructionItemById(idCurrentConstructionItem);

        constructionItemService.getNext(currentConstructionItem).ifPresentOrElse(
                nextConstructionItem -> {
                    showNextConstructionItem(nextConstructionItem, chatId, commonInfo);
                    constructionItemDtoService.saveConstructionItemDto(nextConstructionItem);
                },
                () -> {
                    ConstructionItem constructionItem = constructionItemService.getFirst().get();
                    showNextConstructionItem(constructionItem, chatId, commonInfo);
                    constructionItemDtoService.saveConstructionItemDto(constructionItem);
                }
        );
    }

    private void showNextConstructionItem(ConstructionItem nextConstructionItem,
                                          Long chatId,
                                          CommonInfo commonInfo) {

        constructionItemDtoService.saveConstructionItemDto(nextConstructionItem);

        EditMessageText titleText = AnswerMethodFactory.getEditMessageText(chatId,
                commonInfo.getMessageId() - 2,
                nextConstructionItem.getTitle());

        EditMessageMedia sendPhoto = AnswerMethodFactory.getEditMessageMedia(chatId,
                commonInfo.getMessageId() - 1,
                nextConstructionItem.getPhotoFileId());

        EditMessageText descriptionText = AnswerMethodFactory.getEditMessageText(chatId,
                commonInfo.getMessageId(),
                nextConstructionItem.getDescription(),
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

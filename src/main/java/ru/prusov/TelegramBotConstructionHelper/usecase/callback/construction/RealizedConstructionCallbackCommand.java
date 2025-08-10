package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ConstructionItemService;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.NO_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealizedConstructionCallbackCommand implements CallbackCommand {
    private final TelegramClient client;
    private final ConstructionItemService constructionItemService;

    @Override
    public String command() {
        return REALIZED_CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        log.info("started method {}", RealizedConstructionCallbackCommand.class.getSimpleName());
        ConstructionItem constructionItem = constructionItemService.getFirst();
        if (constructionItem != null) {
            sendContent(commonInfo, constructionItem);
        } else {
            sendEmptyContent(commonInfo);
        }
    }

    private void sendEmptyContent(CommonInfo commonInfo) {
        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId(),
                NO_CONSTRUCTION_ITEM,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)
                )
        );
        try {
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendContent(CommonInfo commonInfo, ConstructionItem constructionItem) {
        EditMessageText titleText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId(),
                constructionItem.getTitle());
        SendPhoto sendPhoto = AnswerMethodFactory.getSendPhoto(
                commonInfo.getChatId(),
                constructionItem.getPhotoFileId());
        SendMessage descriptionText = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                constructionItem.getDescription(),
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
            throw new RuntimeException(e);
        }
    }
}

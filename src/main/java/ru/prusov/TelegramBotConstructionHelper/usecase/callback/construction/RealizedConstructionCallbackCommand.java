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

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.NO_CONSTRUCTION_ITEM;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealizedConstructionCallbackCommand extends AbstractCallbackCommand {

    private final ConstructionItemService constructionItemService;
    private final ConstructionItemDtoService constructionItemDtoService;

    @Override
    public String command() {
        return REALIZED_CONSTRUCTION;
    }


    @Override
    protected void doExecute(CommonInfo commonInfo) {
        deleteAllMessage(commonInfo.getChatId());
        constructionItemService.getFirst().ifPresentOrElse(
                constructionItem -> {
                    constructionItemDtoService.saveConstructionItemDto(constructionItem);
                    sendContent(commonInfo, constructionItem);
                }, () -> {
                    sendEmptyContent(commonInfo);
                }
        );
    }

    @Override
    protected Logger log() {
        return log;
    }

    private void sendEmptyContent(CommonInfo commonInfo) {

        replyAndTrack(commonInfo.getChatId(), NO_CONSTRUCTION_ITEM, KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)),
                commonInfo.getMessageId() + 1);
    }

    private void sendContent(CommonInfo commonInfo, ConstructionItem constructionItem) {
        Long chatId = commonInfo.getChatId();
        replyAndTrack(chatId,
                constructionItem.getTitle(),
                commonInfo.getMessageId() + 1);
        sendPhotoAndTrack(chatId,
                constructionItem.getPhotoFileId(),
                commonInfo.getMessageId() + 2);
        replyAndTrack(
                chatId,
                constructionItem.getDescription(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Предыдущий", "Следующий", "Назад"),
                        List.of(2, 1),
                        List.of(PREV_CONSTRUCTION, NEXT_CONSTRUCTION, CONSTRUCTION)),
                commonInfo.getMessageId() + 3);
    }
}

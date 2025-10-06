package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.common.TelegramSender;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.MessageIDKeeperService;

import java.util.List;

public abstract class AbstractState implements State{
    protected TelegramSender sender;
    protected MessageIDKeeperService messageIDKeeperService;

    @Autowired
    protected void setDependencies(TelegramSender sender, MessageIDKeeperService messageIDKeeperService){
        this.sender = sender;
        this.messageIDKeeperService = messageIDKeeperService;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        try {
            doExecute(commonInfo);
        } catch (Exception e) {
            log().error("State  command: {} failed {} for chat {}", getClass().getSimpleName(), state(), commonInfo.getChatId(), e);
            throw e;
        }
    }

    protected void deleteAllMessage(Long chatId) {
        List<Integer> integers = messageIDKeeperService.deleteAllMessageId(chatId);
        if (integers != null && !integers.isEmpty()) {
            sender.execute(new DeleteMessages(chatId.toString(), integers));
            log().info("State: {} Deleted {} messages for chat {}", getClass().getSimpleName(), integers.size(), chatId);
        }
        log().info("State: {} No messages to delete for chat {}", getClass().getSimpleName(), chatId);
    }


    protected void reply(Long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        sender.execute(AnswerMethodFactory.getSendMessage(chatId, text, keyboardMarkup));
    }

    protected void reply(Long chatId, String text) {
        sender.execute(AnswerMethodFactory.getSendMessage(chatId, text));
    }

    protected void sendPhoto(Long chatId, String photoPath) {
        sender.execute(AnswerMethodFactory.getSendPhoto(chatId, photoPath));
    }

    protected void editReply(Long chatId, String text, int messageId){
        sender.execute(AnswerMethodFactory.getEditMessageText(chatId, messageId, text, null));
    }

    protected void editReply(Long chatId, String text, InlineKeyboardMarkup keyboardMarkup, int messageId){
        sender.execute(AnswerMethodFactory.getEditMessageText(chatId, messageId, text, keyboardMarkup));
    }

    protected void editSendPhoto(Long chatId, String photoPath, int messageId){
        sender.execute(AnswerMethodFactory.getEditMessageMedia(chatId, messageId, photoPath));
    }

    protected void sendPhotoAndTrack(Long chatId, String photoPath, int messageId) {
        sendPhoto(chatId, photoPath);
        trackSentMessageId(chatId, messageId);
    }

    protected void replyAndTrack(Long chatId, String text, InlineKeyboardMarkup keyboard, int fallbackMessageId) {
        reply(chatId, text, keyboard);
        trackSentMessageId(chatId, fallbackMessageId);
    }

    protected void replyAndTrack(Long chatId, String text, int fallbackMessageId) {
        reply(chatId, text);
        trackSentMessageId(chatId, fallbackMessageId);
    }

    protected void trackSentMessageId(Long chatId, int candidateMessageId) {
        if (messageIDKeeperService.hasMessageId(chatId)) {
            Integer lastMessageId = messageIDKeeperService.getLastMessageId(chatId);
            messageIDKeeperService.addMessageId(chatId, lastMessageId + 1);
            log().info("Command: {} Tracked incremented messageId {} for chat {}", getClass().getSimpleName(), lastMessageId + 1, chatId);
        } else {
            messageIDKeeperService.addMessageId(chatId, candidateMessageId);
            log().info("Command: {} Tracked fallback messageId {} for chat {}", getClass().getSimpleName(), candidateMessageId, chatId);
        }
    }

    protected abstract void doExecute(CommonInfo commonInfo);
    protected abstract Logger log();
}

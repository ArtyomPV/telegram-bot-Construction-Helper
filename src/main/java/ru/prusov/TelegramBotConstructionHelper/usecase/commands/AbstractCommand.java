package ru.prusov.TelegramBotConstructionHelper.usecase.commands;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.prusov.TelegramBotConstructionHelper.common.TelegramSender;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.MessageIDKeeperService;

public abstract class AbstractCommand implements Command {
    protected MessageIDKeeperService messageIDKeeperService;
    protected TelegramSender sender;

    @Autowired
    protected void setDependencies(MessageIDKeeperService messageIDKeeperService, TelegramSender sender) {
        this.messageIDKeeperService = messageIDKeeperService;
        this.sender = sender;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        try {
            doExecute(commonInfo);
        } catch (Exception e) {
            log().error("Command failed {} for chat {}", command(), commonInfo.getChatId(), e);
            throw e;
        }
    }

    protected void addMessageId(Long chatId, Integer messageId) {
        messageIDKeeperService.addMessageId(chatId, messageId);
    }

    protected void deleteAllMessage(Long chatId) {
        sender.execute(new DeleteMessages(chatId.toString(), messageIDKeeperService.deleteAllMessageId(chatId)));
    }

    protected void reply(Long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        sender.execute(AnswerMethodFactory.getSendMessage(chatId, text, keyboardMarkup));
    }

    protected void reply(Long chatId, String text) {
        sender.execute(AnswerMethodFactory.getSendMessage(chatId, text));
    }

    protected void sendPhoto(Long chatId, String photoPath) {
        SendPhoto sendPhoto = AnswerMethodFactory.getSendPhoto(chatId, photoPath);
        sender.execute(sendPhoto);
    }


    protected abstract Logger log();

    protected abstract void doExecute(CommonInfo commonInfo);
}

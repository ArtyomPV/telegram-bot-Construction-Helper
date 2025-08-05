package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.commands.UserCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.routers.CommandRouter;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.UNKNOWN_START_MESSAGE;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class TelegramBotService implements LongPollingSingleThreadUpdateConsumer, SpringLongPollingBot {
    private final String botToken;
    private final TelegramClient client;
    private final CommandRouter commandRouter;

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message lastUserMessage = update.getMessage();
            if (update.getMessage().hasText()) {
                String lastUserMessageText = lastUserMessage.getText();
                CommonInfo commonInfo = getCommonInfo(update);
                if (lastUserMessageText.startsWith("/")) {
                    handleCommand(lastUserMessageText, commonInfo);
                }

            }
        }
        if (update.hasMessage() && update.getMessage().hasText()) {
            log.info("it is working");
        }
    }

    private void handleCommand(String lastUserMessageText, CommonInfo commonInfo) {
        UserCommand userCommand = UserCommand.fromString(lastUserMessageText);
        commandRouter.getHandler(userCommand).ifPresentOrElse(h -> {
            h.execute(commonInfo);
        }, () -> {
            unknownActionHandler(commonInfo.getChatId());
        });

    }

    private void unknownActionHandler(Long chatId) {
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId, UNKNOWN_START_MESSAGE);
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

    }

    private CommonInfo getCommonInfo(Update update) {
        return CommonInfo.builder()
                .chatId(update.getMessage().getChatId())
                .userFromTelegram(update.getMessage().getFrom())
                .messageText(update.getMessage().getText())
                .build();
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}

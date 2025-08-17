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
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.commands.UserCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.routers.CallbackRouter;
import ru.prusov.TelegramBotConstructionHelper.usecase.routers.CommandRouter;
import ru.prusov.TelegramBotConstructionHelper.usecase.routers.StateRouter;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.constants.TextConstants.UNKNOWN_START_MESSAGE;

@Slf4j
@Service
@EnableAsync
@RequiredArgsConstructor
public class TelegramBotService implements LongPollingSingleThreadUpdateConsumer, SpringLongPollingBot {
    private final String botToken;
    private final TelegramClient client;
    private final CommandRouter commandRouter;
    private final CallbackRouter callbackRouter;
    private final StateService stateService;
    private final StateRouter stateRouter;
    private final PhotoService photoService;

    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {
            Message lastUserMessage = update.getMessage();
            if (lastUserMessage.hasText()) {
                String lastUserMessageText = lastUserMessage.getText();
                CommonInfo commonInfo = getCommonInfo(lastUserMessage);
                if (lastUserMessageText.startsWith("/")) {
                    handleCommand(lastUserMessageText, commonInfo);
                    return;
                }
                Long chatId = commonInfo.getChatId();
                UserState userState = stateService.getUserStateByChatId(chatId);
                stateRouter.getHandler(userState).ifPresentOrElse(handle -> {
                    handle.handleState(commonInfo);
                }, () -> {
                    unknownActionHandler(chatId);
                });


            } else if (lastUserMessage.hasPhoto()) {
                String caption = lastUserMessage.getCaption();
                CommonInfo commonInfo = getCommonInfo(lastUserMessage);
                commonInfo.setDocumentName(caption);
                List<PhotoSize> photos = lastUserMessage.getPhoto();
                PhotoSize photoSize = photos.get(photos.size() - 1);
                photoService.loadPhotoFromMessage(photoSize, commonInfo.getChatId(), caption);
                UserState userState = stateService.getUserStateByChatId(lastUserMessage.getChatId());

                stateRouter.getHandler(userState).ifPresentOrElse(handler -> handler.handleState(commonInfo),
                        () -> unknownActionHandler(commonInfo.getChatId()));

            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            CommonInfo commonInfo = getCommonInfo(callbackQuery);
            handleCallbackQuery(commonInfo);
        }
    }

    private void handleCallbackQuery(CommonInfo commonInfo) {
        callbackRouter.getHandler(commonInfo.getMessageText()).ifPresentOrElse(handler -> {
            handler.execute(commonInfo);
        }, () -> {
            unknownActionHandler(commonInfo.getChatId());
        });
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

    private CommonInfo getCommonInfo(Message message) {
        return CommonInfo.builder()
                .chatId(message.getChatId())
                .userFromTelegram(message.getFrom())
                .messageText(message.getText())
                .messageId(message.getMessageId())
                .build();
    }

    private CommonInfo getCommonInfo(CallbackQuery callbackQuery) {
        return CommonInfo.builder()
                .chatId(callbackQuery.getMessage().getChatId())
                .messageText(callbackQuery.getData())
                .messageId(callbackQuery.getMessage().getMessageId())
                .userFromTelegram(callbackQuery.getFrom())
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

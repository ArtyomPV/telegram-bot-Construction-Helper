package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.constants.TextConstants;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommand implements Command{
    private final TelegramClient client;
    @Override
    public UserCommand command() {
        return UserCommand.START;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();
        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(chatId, TextConstants.START_MESSAGE);
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}

package ru.prusov.TelegramBotConstructionHelper.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiValidationException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.common.exceprtions.TelegramClientRuntimeException;

import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramSender {

    private final TelegramClient client;

    public <T extends Serializable> T execute(BotApiMethod<T> method) {
        try {
            return client.execute(method);
        } catch (TelegramApiException e) {
            log.error("Telegram Api error on {}: {}", method.getMethod(), e.getMessage(), e);
            throw new TelegramClientRuntimeException("Telegram call failed: " + method.getMethod(), e);
        }
    }

    public Message execute(SendPhoto sendPhoto) {
        try {
            return client.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Telegram Api error on {}: {}", sendPhoto.getMethod(), e.getMessage(), e);
            throw new TelegramClientRuntimeException("Telegram call failed: " + sendPhoto.getMethod(), e);
        }
    }
}

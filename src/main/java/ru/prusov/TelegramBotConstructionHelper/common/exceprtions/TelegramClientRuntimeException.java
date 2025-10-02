package ru.prusov.TelegramBotConstructionHelper.common.exceprtions;

public class TelegramClientRuntimeException extends RuntimeException{
    public TelegramClientRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}

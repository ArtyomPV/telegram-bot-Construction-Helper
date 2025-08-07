package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;

public interface CallbackCommand {
    String command();

    void execute(CommonInfo commonInfo);
}

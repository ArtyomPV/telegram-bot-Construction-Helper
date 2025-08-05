package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;

public interface Command {
    UserCommand command();

    void execute(CommonInfo commonInfo);
}

package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import org.springframework.scheduling.annotation.Async;

public interface State {
    UserState state();

    @Async
    void handleState();
}

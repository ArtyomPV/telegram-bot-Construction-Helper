package ru.prusov.TelegramBotConstructionHelper.usecase.state;

import org.springframework.scheduling.annotation.Async;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;

public interface State {
    UserState state();

    @Async
    void handleState(CommonInfo commonInfo);
}

package ru.prusov.TelegramBotConstructionHelper.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;

import java.util.*;

@Component
public class CallbackRouter {
    private Map<String, CallbackCommand> handlerMap = new HashMap<>();

    public CallbackRouter(List<CallbackCommand> handlers) {
        handlers.forEach(
                h -> {
                    handlerMap.put(h.command(), h);
                }
        );
    }

    public Optional<CallbackCommand> getHandler(String callbackDataCommand) {
        return Optional.ofNullable(handlerMap.get(callbackDataCommand));
    }
}

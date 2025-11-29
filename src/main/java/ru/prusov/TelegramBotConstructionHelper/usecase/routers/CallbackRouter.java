package ru.prusov.TelegramBotConstructionHelper.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONTRACTS_PAGE;

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
        if (callbackDataCommand.startsWith(CONTRACTS_PAGE)) {
            callbackDataCommand = callbackDataCommand.substring(0, callbackDataCommand.length() - 1);
        }
        return Optional.ofNullable(handlerMap.get(callbackDataCommand));
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.routers;

import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.usecase.commands.Command;
import ru.prusov.TelegramBotConstructionHelper.usecase.commands.UserCommand;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class CommandRouter {
    private Map<UserCommand, Command> handleMap = new EnumMap<>(UserCommand.class);

    public CommandRouter(List<Command> handlers){
        handlers.forEach(h -> {
            handleMap.put(h.command(), h);
        });
    }

    public Optional<Command> getHandler(UserCommand userCommand){
        return Optional.ofNullable(handleMap.get(userCommand));
    }
}

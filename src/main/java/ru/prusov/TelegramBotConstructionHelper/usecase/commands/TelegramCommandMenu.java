package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.UserService;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramCommandMenu {
    private final TelegramClient sender;
    private final UserService userService;

    @PostConstruct
    public void registerCommands() {

        try {

            List<BotCommand> commands = List.of(
                    new BotCommand("start", "Приветствие"),
                    new BotCommand("info", "Информация, как пользоваться ботом"),
                    new BotCommand("contacts", "Связаться с нами")
            );
            sender.execute(new SetMyCommands(commands, BotCommandScopeDefault.builder().build(), null));
        } catch (Exception e) {
            throw new RuntimeException("Failed to register commands: " + e.getMessage(), e);
        }
    }

}

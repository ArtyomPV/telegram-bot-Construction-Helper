package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCommand {
    START("/start");

    private final String command;

    public static UserCommand fromString(String command){
        for(UserCommand userCommand: UserCommand.values()){
            if(command.equals(userCommand.getCommand())){
                return userCommand;
            }
        }
        return null;
    }
}

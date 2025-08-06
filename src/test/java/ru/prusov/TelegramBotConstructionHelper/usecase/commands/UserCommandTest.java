package ru.prusov.TelegramBotConstructionHelper.usecase.commands;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("Test find UserCommand")
class UserCommandTest {


    @Test
    void shouldReturnStartFromStringWhenCommandIsStart(){
        assertEquals(UserCommand.START, UserCommand.fromString("/start"));
    }

    @Test
    void shouldReturnNullFromStringWhenCommandIsUnknown(){
        assertEquals(null, UserCommand.fromString("/unknown"));
    }

}
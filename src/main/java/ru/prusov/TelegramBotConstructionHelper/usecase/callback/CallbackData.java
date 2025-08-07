package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CallbackData {
    public final static String CONSTRUCTION = "construction";
    public final static String ENGINEERING = "engineering";
    public final static String AUTOMATIZATION = "automatization";
    public static final String START = "start";


}

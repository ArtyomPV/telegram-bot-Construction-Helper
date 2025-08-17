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
    public static final String SETTINGS = "settings";
    public static final String LOGO_CHANGE = "logo_change";
    public static final String REALIZED_CONSTRUCTION = "realized_construction";
    public static final String ARTICLE_CONSTRUCTION = "article_construction";
    public static final String  CONTACT_CONSTRUCTION = "contact_construction";
    public static final String PREV_CONSTRUCTION = "prev_construction";
    public static final String NEXT_CONSTRUCTION = "next_construction";
    public static final String ADD_CONSTRUCTION_ITEM = "add_construction_item";


}

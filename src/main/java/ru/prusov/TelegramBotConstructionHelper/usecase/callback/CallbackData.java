package ru.prusov.TelegramBotConstructionHelper.usecase.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CallbackData {
    public static final String START = "start";
    public static final String SETTINGS = "settings";
    public final static String ENGINEERING = "engineering";
    public static final String LOGO_CHANGE = "logo_change";

    public final static String CONSTRUCTION = "construction";
    public static final String PREV_CONSTRUCTION = "prev_construction";
    public static final String NEXT_CONSTRUCTION = "next_construction";
    public static final String ARTICLE_CONSTRUCTION = "article_construction";
    public static final String CONTACT_CONSTRUCTION = "contact_construction";
    public static final String REALIZED_CONSTRUCTION = "realized_construction";
    public static final String ADD_CONSTRUCTION_ITEM = "add_construction_item";
    public static final String ADD_ARTICLE = "add_article";

    public final static String AUTOMATIZATION = "automatization";
    public final static String REALIZED_AUTOMATICS = "realized_automatics";
    public final static String ARTICLE_AUTOMATICS = "article_automatics";
    public final static String CONTACT_AUTOMATICS = "contact_automatics";


}

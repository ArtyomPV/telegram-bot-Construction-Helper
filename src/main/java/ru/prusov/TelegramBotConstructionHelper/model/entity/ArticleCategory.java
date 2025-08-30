package ru.prusov.TelegramBotConstructionHelper.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ArticleCategory {
    CONSTRUCTION("construction"),
    ENGINEERING("engineering"),
    AUTOMATION("automation");
    private String name;
}

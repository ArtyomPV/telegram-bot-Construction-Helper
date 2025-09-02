package ru.prusov.TelegramBotConstructionHelper.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ArticleCategory {
    CONSTRUCTION_CAT("construction_cat"),
    ENGINEERING_CAT("engineering_cat"),
    AUTOMATION_CAT("automation_cat");
    private final String name;

    public ArticleCategory getFromString(String category){
        return Arrays.stream(ArticleCategory.values())
                .filter(articleCategory -> category.equals(articleCategory.name))
                .findFirst().get();
    }
}

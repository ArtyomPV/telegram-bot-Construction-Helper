package ru.prusov.TelegramBotConstructionHelper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
@Component
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String description;
    private ArticleCategory category;

    public void cleanFields(){
        id = null;
        title = null;
        description = null;
        category = null;
    }
}

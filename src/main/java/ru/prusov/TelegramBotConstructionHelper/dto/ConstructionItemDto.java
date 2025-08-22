package ru.prusov.TelegramBotConstructionHelper.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ConstructionItemDto {
    Long id;
    String title;
    String photoFileId;
    String description;
}

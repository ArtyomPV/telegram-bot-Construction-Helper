package ru.prusov.TelegramBotConstructionHelper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommonInfo {
    private Long chatId;
    private String messageText;
    private Integer messageId;
    private User userFromTelegram;
    private String documentName;
    private String fileId;
}

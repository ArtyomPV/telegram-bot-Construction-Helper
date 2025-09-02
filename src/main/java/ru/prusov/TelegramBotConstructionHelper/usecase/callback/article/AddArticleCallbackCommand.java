package ru.prusov.TelegramBotConstructionHelper.usecase.callback.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;


import java.util.Arrays;
import java.util.List;


import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ADD_ARTICLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddArticleCallbackCommand implements CallbackCommand {

    private final TelegramClient client;
    private final StateService stateService;

    @Override
    public String command() {
        return ADD_ARTICLE;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        StringBuilder textMessage = new StringBuilder("Введите категорию статьи: \n");
        ArticleCategory[] values = ArticleCategory.values();
        int index = 1;
        for (ArticleCategory category : values) {
            textMessage.append(index)
                    .append(". ")
                    .append(category.getName())
                    .append("\n");
            index++;
        }

        List<String> buttonData = Arrays.stream(ArticleCategory.values()).map(ArticleCategory::getName)
                .toList();
        buttonData.forEach(System.out::println);


        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId(),
                textMessage.toString(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Строительство", "Инженерные сети", "Автоматика"),
                        List.of(1, 1, 1),
                        buttonData
                )
        );
        try {
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Request failed: object name - class {}",
                    AddArticleCallbackCommand.class.getSimpleName());
        }
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.callback.automation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ArticleService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleAutomaticsCallbackCommand implements CallbackCommand {
    private final String TITLE_CONTENT = "Оглавление статей: ";
    private final String NO_CONTENT = """
            Раздел не содержит статей!
            
            Зайдите позже...
            """;

    private final TelegramClient client;
    private final ArticleService articleService;
    private final StateService stateService;

    @Override
    public String command() {
        return ARTICLE_AUTOMATICS;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        List<Article> allByCategory = articleService.findAllByCategory(ArticleCategory.AUTOMATION_CAT);
        if (allByCategory == null || allByCategory.isEmpty()) {
            sendEmptyContent(commonInfo);
        } else {
            sendArticleMessage(commonInfo, allByCategory);
        }

    }


    private void sendArticleMessage(CommonInfo commonInfo, List<Article> allByCategory) {
        Long chatId = commonInfo.getChatId();

        StringBuilder titlesAllArticleCategoryAutomatics = new StringBuilder(TITLE_CONTENT);

        allByCategory.forEach(article -> {
            titlesAllArticleCategoryAutomatics.append("\n")
                    .append(article.getId())
                    .append(". ")
                    .append(article.getTitle())
                    .append("\n");
        });
        titlesAllArticleCategoryAutomatics.append("\n\n")
                .append("Для просмотра статьи введите её номер.");

        stateService.setUserStateByChatId(chatId, UserState.WAITING_AUTOMATION_ARTICLE_NUMBER);
        log.info("Change user`s state to {}", UserState.WAITING_AUTOMATION_ARTICLE_NUMBER);

        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                chatId,
                titlesAllArticleCategoryAutomatics.toString(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)
                )
        );

        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            sendLogError(e);
            throw new RuntimeException(e);
        }

    }

    private void sendEmptyContent(CommonInfo commonInfo) {
        SendMessage sendEmptyMessage = AnswerMethodFactory.getSendMessage(
                commonInfo.getChatId(),
                NO_CONTENT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)
                )
        );
        try {
            client.execute(sendEmptyMessage);
        } catch (TelegramApiException e) {
            sendLogError(e);
        }
    }

    private static void sendLogError(TelegramApiException e) {
        log.error("Don`t execute method: {}, {}", ArticleAutomaticsCallbackCommand.class.getSimpleName(), e.getMessage());
    }

}


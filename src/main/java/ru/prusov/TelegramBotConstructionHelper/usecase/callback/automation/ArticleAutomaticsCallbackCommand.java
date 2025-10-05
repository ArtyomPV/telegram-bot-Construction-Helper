package ru.prusov.TelegramBotConstructionHelper.usecase.callback.automation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.article.ArticleConstructionService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory.CONSTRUCTION_CAT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleAutomaticsCallbackCommand extends AbstractCallbackCommand {
    private final String TITLE_CONTENT = "Оглавление статей: ";
    private final String NO_CONTENT = """
            Раздел не содержит статей!
            
            Зайдите позже...
            """;

    private final ArticleConstructionService articleService;
    private final StateService stateService;

    @Override
    public String command() {
        return ARTICLE_AUTOMATICS;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        deleteAllMessage(commonInfo.getChatId());
        List<Article> allByCategory = articleService.findAllByCategory(ArticleCategory.CONSTRUCTION_CAT);
        if (allByCategory == null || allByCategory.isEmpty()) {
            sendEmptyContent(commonInfo);
        } else {
            sendArticleMessage(commonInfo, allByCategory);
        }
    }

    @Override
    protected Logger log() {
        return log;
    }

    private void sendArticleMessage(CommonInfo commonInfo, List<Article> allByCategory) {
        Long chatId = commonInfo.getChatId();

        StringBuilder titlesAllArticleCategoryConstruction = new StringBuilder(TITLE_CONTENT);

        allByCategory.forEach(article -> {
            titlesAllArticleCategoryConstruction.append("\n");
            titlesAllArticleCategoryConstruction.append(article.getId());
            titlesAllArticleCategoryConstruction.append(". ");
            titlesAllArticleCategoryConstruction.append(article.getTitle());
            titlesAllArticleCategoryConstruction.append("\n");
        });
        titlesAllArticleCategoryConstruction.append("\n\n");
        titlesAllArticleCategoryConstruction.append("Для просмотра статьи введите её номер.");

        stateService.setUserStateByChatId(chatId, UserState.WAITING_AUTOMATION_ARTICLE_NUMBER);
        log.info("Change user`s state to {}", UserState.WAITING_AUTOMATION_ARTICLE_NUMBER);

        replyAndTrack(chatId,
                titlesAllArticleCategoryConstruction.toString(),
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)),
                commonInfo.getMessageId() + 1);
    }

    private void sendEmptyContent(CommonInfo commonInfo) {
        replyAndTrack(commonInfo.getChatId(),
                NO_CONTENT,
                KeyboardFactory.getInlineKeyboard(
                        List.of("Назад"),
                        List.of(1),
                        List.of(CONSTRUCTION)),
                commonInfo.getMessageId() + 1
        );
    }
}


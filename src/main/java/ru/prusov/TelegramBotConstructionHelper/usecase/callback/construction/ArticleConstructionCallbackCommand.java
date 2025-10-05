package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.article.ArticleService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory.CONSTRUCTION_CAT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ARTICLE_CONSTRUCTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONSTRUCTION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleConstructionCallbackCommand extends AbstractCallbackCommand {

    private final String TITLE_CONTENT = "Оглавление статей: ";
    private final String NO_CONTENT = """
            Раздел не содержит статей!
            
            Зайдите позже...
            """;

    private final ArticleService articleService;
    private final StateService stateService;

    @Override
    public String command() {
        return ARTICLE_CONSTRUCTION;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        deleteAllMessage(commonInfo.getChatId());
         /*
        Получить список всех статей из БД
        Предложить, пользователю выбрать статью по его id
        Необходимо ввести id
         */

        List<Article> allByCategory = articleService.findAllByCategory(CONSTRUCTION_CAT);
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

        stateService.setUserStateByChatId(chatId, UserState.WAITING_CONSTRUCTION_ARTICLE_NUMBER);
        log.info("Change user`s state to {}", UserState.WAITING_CONSTRUCTION_ARTICLE_NUMBER);

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

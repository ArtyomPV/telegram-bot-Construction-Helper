package ru.prusov.TelegramBotConstructionHelper.usecase.callback.construction;

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
import ru.prusov.TelegramBotConstructionHelper.usecase.services.ArticleConstructionService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ARTICLE_CONSTRUCTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.CONSTRUCTION;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleConstructionCallbackCommand implements CallbackCommand {

    private final String TITLE_CONTENT = "Оглавление статей: ";
    private final String NO_CONTENT = """
            Раздел не содержит статей!
            
            Зайдите позже...
            """;

    private final TelegramClient client;
    private final ArticleConstructionService articleConstructionService;
    private final StateService stateService;

    @Override
    public String command() {
        return ARTICLE_CONSTRUCTION;
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        /*
        Получить список всех статей из БД
        Предложить, пользователю выбрать статью по его id
        Необходимо ввести id
         */

        List<Article> allByCategory = articleConstructionService.findAllByCategory(ArticleCategory.CONSTRUCTION);
        if (allByCategory == null || allByCategory.isEmpty()) {
            sendEmptyContent(commonInfo);
        } else {
            sendArticleMessage(commonInfo, allByCategory);
        }

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

        SendMessage sendMessage = AnswerMethodFactory.getSendMessage(
                chatId,
                titlesAllArticleCategoryConstruction.toString(),
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
        log.error("Don`t execute method: {}", e.getMessage());
    }

}

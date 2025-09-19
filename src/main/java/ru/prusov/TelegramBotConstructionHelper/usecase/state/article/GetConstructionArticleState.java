package ru.prusov.TelegramBotConstructionHelper.usecase.state.article;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.article.ArticleService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;
import java.util.Optional;

import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.ARTICLE_CONSTRUCTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Component
@RequiredArgsConstructor
public class GetConstructionArticleState implements State {

    private final String NO_CONTENT = "Статьи нет по указанной позиции: ";
    private final TelegramClient client;
    private final ArticleService articleService;

    @Override
    public UserState state() {
        return UserState.WAITING_CONSTRUCTION_ARTICLE_NUMBER;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        StringBuilder content = new StringBuilder();
        Long chatId = commonInfo.getChatId();
        String messageText = commonInfo.getMessageText();
        Optional<Article> articleByIndex = articleService.getArticleByIndex(Long.parseLong(commonInfo.getMessageText()), ArticleCategory.CONSTRUCTION_CAT);
        articleByIndex.ifPresentOrElse(article -> {
                    content.append(article.getTitle())
                            .append("\n\n")
                            .append(article.getDescription());

                },
                () -> {
                    content.append(NO_CONTENT)
                            .append(messageText);
                }
        );
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(content.toString())
                .replyMarkup(KeyboardFactory.getInlineKeyboard(
                        List.of("Назад", "Главное меню"),
                        List.of(2),
                        List.of(ARTICLE_CONSTRUCTION, START)
                ))
                .build();
        try {
            client.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }




}

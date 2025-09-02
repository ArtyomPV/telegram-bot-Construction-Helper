package ru.prusov.TelegramBotConstructionHelper.usecase.callback.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_ARTICLE_TITLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleCategoryConstructionCallbackData implements CallbackCommand {

    private final TelegramClient client;
    private final StateService stateService;
    private final ArticleDto articleDto;

    private final String TEXT_MESSAGE = "Введите название статьи ";

    @Override
    public String command() {
        return ArticleCategory.CONSTRUCTION_CAT.getName();
    }

    @Override
    public void execute(CommonInfo commonInfo) {
        articleDto.setCategory(ArticleCategory.CONSTRUCTION_CAT);
        stateService.setUserStateByChatId(commonInfo.getChatId(), WAITING_ARTICLE_TITLE);
        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId(),
                TEXT_MESSAGE
        );
        try {
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Request failed: object name - class {}",
                    ArticleCategoryConstructionCallbackData.class.getSimpleName());
        }
    }
}

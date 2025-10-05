package ru.prusov.TelegramBotConstructionHelper.usecase.callback.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.AbstractCallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackCommand;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;

import static ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory.ENGINEERING_CAT;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_ARTICLE_TITLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleCategoryEngineeringCallbackData extends AbstractCallbackCommand {

    private final String TEXT_MESSAGE = "Введите название статьи ";

    private final TelegramClient client;
    private final ArticleDto articleDto;
    private final StateService stateService;

    @Override
    public String command() {
        return ENGINEERING_CAT.getName();
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        articleDto.setCategory(ArticleCategory.ENGINEERING_CAT);
        stateService.setUserStateByChatId(commonInfo.getChatId(), WAITING_ARTICLE_TITLE);

        replyAndTrack(
                commonInfo.getChatId(),
                TEXT_MESSAGE,
                commonInfo.getMessageId() + 1
        );
    }

    @Override
    protected Logger log() {
        return log;
    }
}

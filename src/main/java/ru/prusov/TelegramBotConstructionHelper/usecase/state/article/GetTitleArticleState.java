package ru.prusov.TelegramBotConstructionHelper.usecase.state.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.AnswerMethodFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTitleArticleState implements State {

    private final String TEXT_MESSAGE = "Введите текст статьи ";

    private final TelegramClient client;
    private final StateService stateService;
    private final ArticleDto articleDto;

    @Override
    public UserState state() {
        return WAITING_ARTICLE_TITLE;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {

        articleDto.setTitle(commonInfo.getMessageText());
        log.info(articleDto.toString());
        stateService.setUserStateByChatId(commonInfo.getChatId(), WAITING_ARTICLE_DESCRIPTION);
        log.info("User`s status is changed to: {}", WAITING_ARTICLE_DESCRIPTION);

        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 1,
                TEXT_MESSAGE
        );
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId());

        try {
            client.execute(deleteMessage);
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            log.error("Request failed: object name - class {}",
                    GetTitleArticleState.class.getSimpleName()
            );
        }
    }
}

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
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.article.ArticleService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.State;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.NONE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_ARTICLE_DESCRIPTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDescriptionArticleState implements State {
    private final String TEXT_MESSAGE = "Статья сохранена";

    private final TelegramClient client;
    private final StateService stateService;
    private final ArticleService articleService;
    private final ArticleDto articleDto;

    @Override
    public UserState state() {
        return WAITING_ARTICLE_DESCRIPTION;
    }

    @Override
    public void handleState(CommonInfo commonInfo) {
        articleDto.setDescription(commonInfo.getMessageText());
        log.info(articleDto.toString());
        stateService.setUserStateByChatId(commonInfo.getChatId(), NONE);
        articleService.save(articleDto);
        articleDto.cleanFields();
        DeleteMessage deleteMessage = AnswerMethodFactory.getDeleteMessage(
                commonInfo.getChatId(),
                commonInfo.getMessageId()
        );
        EditMessageText editMessageText = AnswerMethodFactory.getEditMessageText(
                commonInfo.getChatId(),
                commonInfo.getMessageId() - 2,
                TEXT_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("В главное меню"),
                        List.of(1),
                        List.of(START)
                )
        );

        try {
            client.execute(deleteMessage);
            client.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }
}

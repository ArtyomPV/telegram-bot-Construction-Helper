package ru.prusov.TelegramBotConstructionHelper.usecase.state.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.factory.KeyboardFactory;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.article.ArticleService;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import java.util.List;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.NONE;
import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.WAITING_ARTICLE_DESCRIPTION;
import static ru.prusov.TelegramBotConstructionHelper.usecase.callback.CallbackData.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetDescriptionArticleState extends AbstractState {
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
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();

        articleDto.setDescription(commonInfo.getMessageText());
        stateService.setUserStateByChatId(chatId, NONE);
        articleService.save(articleDto);
        articleDto.cleanFields();

        replyAndTrack(chatId,
                TEXT_MESSAGE,
                KeyboardFactory.getInlineKeyboard(
                        List.of("В главное меню"),
                        List.of(1),
                        List.of(START)),
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

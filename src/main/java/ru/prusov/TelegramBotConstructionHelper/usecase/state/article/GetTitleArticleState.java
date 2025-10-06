package ru.prusov.TelegramBotConstructionHelper.usecase.state.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.dto.CommonInfo;
import ru.prusov.TelegramBotConstructionHelper.usecase.services.StateService;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.AbstractState;
import ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState;

import static ru.prusov.TelegramBotConstructionHelper.usecase.state.UserState.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetTitleArticleState extends AbstractState {

    private final String TEXT_MESSAGE = "Введите текст статьи ";

    private final TelegramClient client;
    private final StateService stateService;
    private final ArticleDto articleDto;

    @Override
    public UserState state() {
        return WAITING_ARTICLE_TITLE;
    }

    @Override
    protected void doExecute(CommonInfo commonInfo) {
        Long chatId = commonInfo.getChatId();

        articleDto.setTitle(commonInfo.getMessageText());
        stateService.setUserStateByChatId(chatId, WAITING_ARTICLE_DESCRIPTION);

        replyAndTrack(chatId,
                TEXT_MESSAGE,
                commonInfo.getMessageId() + 1);
    }

    @Override
    protected Logger log() {
        return log;
    }
}

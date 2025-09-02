package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ArticleRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAllByCategory(ArticleCategory category) {
        return articleRepository.findAllByCategory(category);
    }

    @Transactional
    public void save(ArticleDto articleDto) {
        try {
            articleRepository.save(Article.builder()
                            .title(articleDto.getTitle())
                            .description(articleDto.getDescription())
                            .category(articleDto.getCategory())
                    .build());
            log.info("Article is saved");
        } catch (Exception e) {
            log.error("Article is not saved");
        }
    }
}

package ru.prusov.TelegramBotConstructionHelper.usecase.services.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.dto.ArticleDto;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleCacheService articleCacheService;


    public List<Article> findAllByCategory(ArticleCategory category) {
        return articleCacheService.getAllConstructionArticle().stream()
                .filter(article -> article.getCategory().equals(category))
                .toList();
    }

    @Transactional
    public void save(ArticleDto articleDto) {
        try {
            articleCacheService.save(Article.builder()
                            .title(articleDto.getTitle())
                            .description(articleDto.getDescription())
                            .category(articleDto.getCategory())
                    .build());
            log.info("Article is saved");
        } catch (Exception e) {
            log.error("Article is not saved");
        }
    }

    @Transactional(readOnly = true)
    public Optional<Article> getArticleByIndex(long index, ArticleCategory category){
        List<Article> articles = findAllByCategory(category);
        return articles.stream()
                .filter(article -> article.getId()==index)
                .findFirst();
    }
}

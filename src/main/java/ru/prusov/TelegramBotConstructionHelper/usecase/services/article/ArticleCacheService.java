package ru.prusov.TelegramBotConstructionHelper.usecase.services.article;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ArticleRepository;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticleCacheService {

    private final ArticleRepository articleRepository;

    private final static String CONSTRUCTION_ALL_ARTICLE_CACHE = "constructionArticleList";

    @Cacheable(value = CONSTRUCTION_ALL_ARTICLE_CACHE, key = "'ALL CONSTRUCTION ARTICLE'")
    @Transactional(readOnly = true)
    public List<Article> getAllConstructionArticle(){
        return articleRepository.findAll().stream()
                .sorted(Comparator.comparingLong(Article::getId))
                .toList();
    }

    @Transactional
    @CacheEvict(value = CONSTRUCTION_ALL_ARTICLE_CACHE, key = "'ALL CONSTRUCTION ARTICLE'")
    public Long save(Article article) {
        return articleRepository.save(article).getId();
    }

    // Пример удаления с инвалидированием кеша
    @Transactional
    @CacheEvict(value = CONSTRUCTION_ALL_ARTICLE_CACHE, key = "'ALL CONSTRUCTION ARTICLE'")
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}

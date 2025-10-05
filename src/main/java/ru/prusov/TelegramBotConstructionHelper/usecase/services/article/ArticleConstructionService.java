package ru.prusov.TelegramBotConstructionHelper.usecase.services.article;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleConstructionService {
    private final ArticleCacheService articleCacheService;
    private final ArticleRepository articleRepository;


    public List<Article> findAllByCategory(ArticleCategory category) {
        return articleCacheService.getAllConstructionArticle().stream()
                .filter(article -> article.getCategory().equals(category))
                .toList();
    }

    public Optional<Article> getArticleByIndex(long index, ArticleCategory category){
        List<Article> articles = findAllByCategory(category);
        return articles.stream()
                .filter(article -> article.getId()==index)
                .findFirst();
    }


}


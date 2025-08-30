package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> findAll(){
        return articleRepository.findAll();
    }

    public List<Article> findAllByCategory(ArticleCategory category){
        return articleRepository.findAllByCategory(category);
    }
}

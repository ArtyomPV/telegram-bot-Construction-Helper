package ru.prusov.TelegramBotConstructionHelper.model.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Article;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ArticleCategory;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Transactional
    @Query("SELECT a FROM Article a WHERE a.category = :category ")
    List<Article> findAllByCategory(ArticleCategory category);
}

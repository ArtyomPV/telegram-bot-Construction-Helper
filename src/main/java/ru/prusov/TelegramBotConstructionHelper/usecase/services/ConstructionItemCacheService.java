package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ConstructionItemRepository;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConstructionItemCacheService {

    private final ConstructionItemRepository constructionItemRepository;

    private final static String ALL_CACHE = "constructionItemsList";

    /**
     * из репозитория получаем список всех объектов
     * @return возвращаем закэшированный, отсортированный по id список объектов
     */
    @Cacheable(value = ALL_CACHE, key = "'ALL'")
    @Transactional(readOnly = true)
    public List<ConstructionItem> getAllCached(){
        return constructionItemRepository.findAll()
                .stream()
                .sorted(Comparator.comparingLong(ConstructionItem::getId))
                .toList();
    }

    // При сохранении инвалидируем кеш списка — следующий вызов getAllCached() перечитает БД и обновит кеш
    @Transactional
    @CacheEvict(value = ALL_CACHE, key = "'ALL'")
    public Long save(ConstructionItem constructionItem) {
        return constructionItemRepository.save(constructionItem).getId();
    }

    // Пример удаления с инвалидированием кеша
    @Transactional
    @CacheEvict(value = ALL_CACHE, key = "'ALL'")
    public void deleteById(Long id) {
        constructionItemRepository.deleteById(id);
    }
}

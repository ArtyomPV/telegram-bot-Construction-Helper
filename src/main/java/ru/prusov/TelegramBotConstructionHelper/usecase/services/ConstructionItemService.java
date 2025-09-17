package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ConstructionItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConstructionItemService {


    private final ConstructionItemRepository constructionItemRepository;
    private final ConstructionItemCacheService cacheService;

    @Transactional(readOnly = true)
    public Optional<ConstructionItem> getConstructionItemById(long id) {
        return cacheService.getAllCached().stream()
                .filter(i -> i.getId() == id)
                .findFirst();
    }

    @Transactional(readOnly = true)
    public Optional<ConstructionItem> getFirst() {
        return cacheService.getAllCached().stream().findFirst();
    }


    @Transactional(readOnly = true)
    public Optional<ConstructionItem> getLast() {
        List<ConstructionItem> all = cacheService.getAllCached();
        if (all.isEmpty()) return Optional.empty();
        return Optional.of(all.get(all.size() - 1));
    }

    /**
     * Находимся на позиции first, этот объект предаем в метод
     *
     * @param currentItem
     * @return
     */
    @Transactional(readOnly = true)
    public Optional<ConstructionItem> getNext(ConstructionItem currentItem) {
        List<ConstructionItem> all = cacheService.getAllCached();
        if (currentItem == null) return all.stream().findFirst();
        return all.stream()
                .filter(i -> i.getId() > currentItem.getId())
                .findFirst();

    }

    @Transactional(readOnly = true)
    public Optional<ConstructionItem> getPrev(ConstructionItem currentItem) {
        List<ConstructionItem> all = cacheService.getAllCached();
        if (currentItem == null) return all.isEmpty() ? Optional.empty() : Optional.of(all.get(all.size() - 1));
        // найти элемент с максимальным id < currentId
        return all.stream()
                .filter(i -> i.getId() < currentItem.getId())
                .reduce((first, second) -> second); // последний из подходящих == предыдущий
    }

    @Transactional
    public Long save(ConstructionItem constructionItem) {
        return cacheService.save(constructionItem);
    }

    @Transactional
    public void deleteById(Long id) {
        cacheService.deleteById(id);
    }
}

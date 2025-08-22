package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ConstructionItemRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConstructionItemService {
    private final ConstructionItemRepository constructionItemRepository;

    @Transactional(readOnly = true)
    public ConstructionItem getConstructionItemById(long id) {
        return constructionItemRepository.findById(id).get();
    }

    public Optional<ConstructionItem> getFirst() {
        return constructionItemRepository.findFirstByOrderByIdAsc();
    }

    public Optional<ConstructionItem> getLast() {
        return constructionItemRepository.findFirstByOrderByIdDesc();
    }


    public Optional<ConstructionItem> getNext(ConstructionItem currentItem) {
        if (currentItem == null) {
            return getFirst();
        }
        return constructionItemRepository.findFirstByIdGreaterThanOrderByIdAsc(currentItem.getId());
    }

    public Optional<ConstructionItem> getPrev(ConstructionItem currentItem) {
        if (currentItem == null) {
            return getLast();
        }
        return constructionItemRepository.findFirstByIdLessThanOrderByIdDesc(currentItem.getId());
    }

    @Transactional
    public Long save(ConstructionItem constructionItem) {
        return constructionItemRepository.save(constructionItem).getId();
    }
}

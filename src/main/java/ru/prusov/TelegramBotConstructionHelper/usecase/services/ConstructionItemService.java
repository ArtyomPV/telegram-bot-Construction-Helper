package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;
import ru.prusov.TelegramBotConstructionHelper.model.repository.ConstructionItemRepository;

@Service
@RequiredArgsConstructor
public class ConstructionItemService {
    private final ConstructionItemRepository constructionItemRepository;

    @Transactional(readOnly = true)
    public ConstructionItem getConstructionItemById(long id) {
        return constructionItemRepository.findById(id).get();
    }

    public ConstructionItem getFirst() {
        return constructionItemRepository.findFirstByOrderByIdAsc().orElse(null);
    }
    public ConstructionItem getNext(ConstructionItem currentItem) {
        if (currentItem == null) {
            return getFirst();
        }
        return constructionItemRepository.findFirstByIdGreaterThanOrderByIdAsc(currentItem.getId()).orElse(null);
    }
    @Transactional
    public Long save(ConstructionItem constructionItem) {
        return constructionItemRepository.save(constructionItem).getId();
    }
}

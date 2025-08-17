package ru.prusov.TelegramBotConstructionHelper.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;

import java.util.Optional;

public interface ConstructionItemRepository extends JpaRepository<ConstructionItem, Long> {
    public Optional<ConstructionItem> findFirstByOrderByIdAsc();
    public Optional<ConstructionItem> findFirstByIdGreaterThanOrderByIdAsc(Long id);
}

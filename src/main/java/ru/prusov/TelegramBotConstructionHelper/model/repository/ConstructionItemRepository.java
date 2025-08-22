package ru.prusov.TelegramBotConstructionHelper.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;

import java.util.Optional;

public interface ConstructionItemRepository extends JpaRepository<ConstructionItem, Long> {
    Optional<ConstructionItem> findFirstByOrderByIdAsc();

    Optional<ConstructionItem> findFirstByIdGreaterThanOrderByIdAsc(Long id);

    Optional<ConstructionItem> findFirstByIdLessThanOrderByIdDesc(Long id);

    Optional<ConstructionItem> findFirstByOrderByIdDesc();
}

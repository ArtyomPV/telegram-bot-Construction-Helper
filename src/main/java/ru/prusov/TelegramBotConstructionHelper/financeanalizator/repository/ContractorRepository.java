package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contractor;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
}

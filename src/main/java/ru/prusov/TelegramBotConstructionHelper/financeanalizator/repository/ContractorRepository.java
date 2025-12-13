package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Contractor;

import java.util.Optional;

public interface ContractorRepository extends JpaRepository<Contractor, Long> {
    @Query("Select c from Contractor c where name = :name")
    Optional<Contractor> findCustomerByName(@Param("name") String name);
}

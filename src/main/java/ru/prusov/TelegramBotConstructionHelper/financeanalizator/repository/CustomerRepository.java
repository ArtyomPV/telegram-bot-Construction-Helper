package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

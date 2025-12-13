package ru.prusov.TelegramBotConstructionHelper.financeanalizator.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.prusov.TelegramBotConstructionHelper.financeanalizator.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Transactional
    @Query("select c from Customer c where c.name = :name")
    Optional<Customer> findCustomerContainsName(@Param("name") String name);
}

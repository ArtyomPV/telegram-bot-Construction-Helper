package ru.prusov.TelegramBotConstructionHelper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TelegramBotConstructionHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramBotConstructionHelperApplication.class, args);
    }

}

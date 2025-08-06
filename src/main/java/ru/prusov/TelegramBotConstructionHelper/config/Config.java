package ru.prusov.TelegramBotConstructionHelper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class Config {
    @Bean
    public String botToken(@Value("${bot.botToken}") String botToken) {
        return botToken;
    }

    @Bean
    public TelegramClient getTelegramClient(@Autowired String botToken) {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean
    public Long adminChatId(@Value("${admin.chatId}") Long adminChatId){
        return adminChatId;
    }
}

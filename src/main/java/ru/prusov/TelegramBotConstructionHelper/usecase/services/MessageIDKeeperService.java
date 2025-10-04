package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class MessageIDKeeperService {

    private Map<Long, CopyOnWriteArrayList<Integer>> messageIdMap = new ConcurrentHashMap<>();;

    public void addMessageId(Long chatId, Integer messageId){
        messageIdMap.computeIfAbsent(chatId,
                k -> new CopyOnWriteArrayList<Integer>())
                .add(messageId);
        log.info("Added messageId: {} for chat {}", messageId, chatId);
    }

    public Integer getLastMessageId(Long chatId){
        CopyOnWriteArrayList<Integer> integers = messageIdMap.get(chatId);
        if(integers == null || integers.isEmpty()){
            throw new NoSuchElementException("No message ids for chat " + chatId);
        }

        return integers.stream().max(Integer::compareTo).orElseThrow();
    }

    public List<Integer> deleteAllMessageId(Long chatId){
        return messageIdMap.remove(chatId);
    }

    public boolean hasMessageId(Long chatId){
        CopyOnWriteArrayList<Integer> integers = messageIdMap.get(chatId);

        return integers != null && !integers.isEmpty();
    }

    public List<Integer> getValue(Long chatId){
        CopyOnWriteArrayList<Integer> integers = messageIdMap.get(chatId);
        return integers == null? List.of():List.copyOf(integers);
    }

    public void getKeys(){
        System.out.println(messageIdMap.keySet());
    }

}

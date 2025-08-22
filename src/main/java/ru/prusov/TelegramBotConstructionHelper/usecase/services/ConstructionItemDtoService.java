package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.prusov.TelegramBotConstructionHelper.dto.ConstructionItemDto;
import ru.prusov.TelegramBotConstructionHelper.model.entity.ConstructionItem;

@Service
@RequiredArgsConstructor
public class ConstructionItemDtoService {

    private final ConstructionItemDto constructionItemDto;

    public void saveConstructionItemDto(ConstructionItem constructionItem) {
        constructionItemDto.setId(constructionItem.getId());
        constructionItemDto.setTitle(constructionItem.getTitle());
        constructionItemDto.setPhotoFileId(constructionItem.getPhotoFileId());
        constructionItemDto.setDescription(constructionItem.getDescription());
    }

    public ConstructionItemDto getConstructionItemDto(){
        return constructionItemDto;
    }
}

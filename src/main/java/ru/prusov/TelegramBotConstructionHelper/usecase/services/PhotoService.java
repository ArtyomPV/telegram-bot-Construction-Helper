package ru.prusov.TelegramBotConstructionHelper.usecase.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.photo.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Photo;
import ru.prusov.TelegramBotConstructionHelper.model.repository.PhotoRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final TelegramClient client;
    private final UserService userService;
    private final PhotoRepository photoRepository;

    public void loadPhotoFromMessage(PhotoSize photoSize, long chatId, String capture) {
        try {
            GetFile getFile = new GetFile(photoSize.getFileId());
            File file = client.execute(getFile);
            Photo photo = new Photo();
            photo.setPhotoId(file.getFileId());
            photo.setUser(userService.getUserByChatId(chatId));
            photo.setPhotoName(capture);
            photoRepository.save(photo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional(readOnly = true)
    public Optional<Photo> getPhotoByPhotoName(String photoName) {
        log.info("Start server getPhotoByPhotoName");
        return photoRepository.findByPhotoName(photoName);
    }
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return getPhotoByPhotoName(name).isPresent();
        // если нужно игнорировать регистр:
        // return thingRepository.existsByNameIgnoreCase(name);
    }
}

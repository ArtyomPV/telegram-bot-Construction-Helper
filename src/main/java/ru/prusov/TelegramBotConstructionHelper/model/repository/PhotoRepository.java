package ru.prusov.TelegramBotConstructionHelper.model.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.prusov.TelegramBotConstructionHelper.model.entity.Photo;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    @Query("select p from Photo p where p.photoName = :photoName")
    Optional<Photo> findByPhotoName(@Param("photoName") String photoName);

//    boolean existsByPhoneName(String name);


}

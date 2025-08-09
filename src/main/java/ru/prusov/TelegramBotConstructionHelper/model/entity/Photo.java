package ru.prusov.TelegramBotConstructionHelper.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String photoId;
    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    private User user;
    private String photoName;
}

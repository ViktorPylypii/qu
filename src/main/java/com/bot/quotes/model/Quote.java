package com.bot.quotes.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@Table(name = "quotes")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(nullable = false)
    private String quote;
    private String characterName;
    private String animeName;
    // позначаємо що можуть зберігатись великі данні в даному випадку картинки щоб не було проблем з їх обробкою
    //  Без анотації @Lob JPA міг би спробувати зберегти великі об’єкти в стандартних типах,
    //  що може призвести до помилок або обмежень, оскільки стандартні типи даних можуть не підтримувати великі розміри.
    //  @Lob автоматично обробляє це для вас, зменшуючи ймовірність помилок.
    @Lob
    @Column(name = "image_data")
    private byte[] imageData;
}

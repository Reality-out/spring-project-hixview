package site.hixview.jpa.entity.supers;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hixview.jpa.entity.PressEntity;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordSnake.PRESS_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.SUBJECT_COUNTRY_SNAKE;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ArticleSuperEntity {
    @Column(unique = true, length = 80, nullable = false)
    private String name;

    @Column(unique = true, length = 400, nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = SUBJECT_COUNTRY_SNAKE, length = 20, nullable = false)
    private String subjectCountry;

    @Column(nullable = false, length = 20)
    private String importance;

    @Column(nullable = false, length = 36)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = PRESS_NUM_SNAKE, nullable = false)
    private PressEntity press;
}

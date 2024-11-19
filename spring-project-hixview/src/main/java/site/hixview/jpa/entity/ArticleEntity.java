package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.ARTICLE;
import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.PRESS_NUM_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.SUBJECT_COUNTRY_SNAKE;

@Entity
@Table(name = ARTICLE)
@Getter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(unique = true, length = 80, nullable = false)
    private String name;

    @Column(unique = true, length = 400, nullable = false)
    private String link;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 20)
    private String classification;

    @Column(name = SUBJECT_COUNTRY_SNAKE, length = 20, nullable = false)
    private String subjectCountry;

    @Column(nullable = false, length = 20)
    private String importance;

    @Column(nullable = false, length = 36)
    private String summary;

    @Column(nullable = false)
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = PRESS_NUM_SNAKE)
    private PressEntity press;

    public ArticleEntity(String name, String link, LocalDate date, String classification, String subjectCountry, String importance, String summary, PressEntity press) {
        this.name = name;
        this.link = link;
        this.date = date;
        this.classification = classification;
        this.subjectCountry = subjectCountry;
        this.importance = importance;
        this.summary = summary;
        this.press = press;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLink(String link) {
        this.link = link;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }

    public void updateClassification(String classification) {
        this.classification = classification;
    }

    public void updateSubjectCountry(String subjectCountry) {
        this.subjectCountry = subjectCountry;
    }

    public void updateImportance(String importance) {
        this.importance = importance;
    }

    public void updateSummary(String summary) {
        this.summary = summary;
    }

    public void updatePress(PressEntity press) {
        this.press = press;
    }
}

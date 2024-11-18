package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = SECOND_CATEGORY_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SecondCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = INDU_CATE_NUM_SNAKE)
    private IndustryCategoryEntity categoryEntity;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = FIR_CATE_NUM_SNAKE)
    private FirstCategoryEntity firstCategoryEntity;

    public void updateKorName(String korName) {
        this.korName = korName;
    }

    public void updateEngName(String engName) {
        this.engName = engName;
    }
}

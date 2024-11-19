package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordCamel.PRESS;
import static site.hixview.aggregate.vo.WordSnake.ENG_NAME_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.KOR_NAME_SNAKE;

@Entity
@Table(name = PRESS)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    public void updateKorName(String korName) {
        this.korName = korName;
    }

    public void updateEngName(String engName) {
        this.engName = engName;
    }
}

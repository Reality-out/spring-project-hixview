package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.ECONOMY_CONTENT_SNAKE;

@Entity
@Table(name = ECONOMY_CONTENT_SNAKE)
@Getter
public class EconomyContentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(unique = true, length = 20, nullable = false)
    private String name;

    public EconomyContentEntity(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}

package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordCamel.POST;

@Entity
@Table(name = POST)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostEntity {
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

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLink(String link) {
        this.link = link;
    }

    public void updateDate(LocalDate date) {
        this.date = date;
    }
}

package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.SITE_MEMBER_SNAKE;

@Entity
@Table(name = SITE_MEMBER_SNAKE)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(unique = true, length = 80, nullable = false)
    private String id;

    @Column(nullable = false, length = 64)
    private String pw;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, length = 80)
    private String email;

    public void updateId(String id) {
        this.id = id;
    }

    public void updatePw(String pw) {
        this.pw = pw;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEmail(String email) {
        this.email = email;
    }
}

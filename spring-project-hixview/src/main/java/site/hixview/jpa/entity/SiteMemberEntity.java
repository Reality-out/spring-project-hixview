package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.SITE_MEMBER_SNAKE;
import static site.hixview.aggregate.vo.WordSnake.VERSION_NUM_SNAKE;

@Entity
@Table(name = SITE_MEMBER_SNAKE)
@Getter
@NoArgsConstructor
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

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SiteMemberEntity siteMember = (SiteMemberEntity) obj;
        return new EqualsBuilder().append(getId(), siteMember.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getId()).toHashCode();
    }

    public SiteMemberEntity(String id, String pw, String name, String email, Long versionNumber) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.versionNumber = versionNumber;
    }

    public SiteMemberEntity(String id, String pw, String name, String email) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.email = email;
    }

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

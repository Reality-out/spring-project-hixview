package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDate;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordCamel.POST;

@Entity
@Table(name = POST)
@Getter
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostEntity post = (PostEntity) obj;
        return new EqualsBuilder().append(getName(), post.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public PostEntity(String name, String link, LocalDate date) {
        this.name = name;
        this.link = link;
        this.date = date;
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
}

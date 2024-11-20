package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EconomyContentEntity economyContent = (EconomyContentEntity) obj;
        return new EqualsBuilder().append(getName(), economyContent.getName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getName()).toHashCode();
    }

    public EconomyContentEntity(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}

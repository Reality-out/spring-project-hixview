package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordCamel.POST;
import static site.hixview.aggregate.vo.WordSnake.VERSION_NUM_SNAKE;

@Entity
@Table(name = POST)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PostEntity post = (PostEntity) obj;
        return new EqualsBuilder().append(getNumber(), post.getNumber()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getNumber()).toHashCode();
    }

    public PostEntity(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public void updateNumber(Long number) {
        this.number = number;
    }
}

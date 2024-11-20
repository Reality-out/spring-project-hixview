package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_CATEGORY_SNAKE)
@Getter
public class IndustryCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryCategoryEntity industryCategory = (IndustryCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getKorName(), industryCategory.getKorName())
                .append(getEngName(), industryCategory.getEngName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKorName())
                .append(getEngName())
                .toHashCode();
    }

    public IndustryCategoryEntity(String korName, String engName) {
        this.korName = korName;
        this.engName = engName;
    }

    public void updateKorName(String korName) {
        this.korName = korName;
    }

    public void updateEngName(String engName) {
        this.engName = engName;
    }
}

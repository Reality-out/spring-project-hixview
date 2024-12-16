package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = INDUSTRY_CATEGORY_SNAKE)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IndustryCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String koreanName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String englishName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndustryCategoryEntity industryCategory = (IndustryCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getEnglishName(), industryCategory.getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getEnglishName())
                .toHashCode();
    }

    public IndustryCategoryEntity(String koreanName, String englishName) {
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }
}

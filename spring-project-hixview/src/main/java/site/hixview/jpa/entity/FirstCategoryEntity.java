package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = FIRST_CATEGORY_SNAKE)
@Getter
@NoArgsConstructor
public class FirstCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String koreanName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String englishName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = INDU_CATE_NUM_SNAKE, nullable = false)
    private IndustryCategoryEntity industryCategory;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FirstCategoryEntity firstCategory = (FirstCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getEnglishName(), firstCategory.getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getEnglishName())
                .toHashCode();
    }

    public FirstCategoryEntity(Long number, String koreanName, String englishName, IndustryCategoryEntity industryCategory) {
        this.number = number;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
    }

    public FirstCategoryEntity(String koreanName, String englishName, IndustryCategoryEntity industryCategory, Long versionNumber) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
        this.versionNumber = versionNumber;
    }

    public FirstCategoryEntity(String koreanName, String englishName, IndustryCategoryEntity industryCategory) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void updateIndustryCategory(IndustryCategoryEntity industryCategory) {
        this.industryCategory = industryCategory;
    }
}

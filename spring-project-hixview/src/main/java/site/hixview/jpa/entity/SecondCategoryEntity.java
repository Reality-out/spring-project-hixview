package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = SECOND_CATEGORY_SNAKE)
@Getter
@NoArgsConstructor
public class SecondCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String koreanName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String englishName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = INDU_CATE_NUM_SNAKE, nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private IndustryCategoryEntity industryCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = FIR_CATE_NUM_SNAKE, nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FirstCategoryEntity firstCategory;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecondCategoryEntity secondCategory = (SecondCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getEnglishName(), secondCategory.getEnglishName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getEnglishName())
                .toHashCode();
    }

    public SecondCategoryEntity(Long number, String koreanName, String englishName, IndustryCategoryEntity industryCategory, FirstCategoryEntity firstCategory) {
        this.number = number;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
        this.firstCategory = firstCategory;
    }

    public SecondCategoryEntity(String koreanName, String englishName, IndustryCategoryEntity industryCategory, FirstCategoryEntity firstCategory, Long versionNumber) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
        this.firstCategory = firstCategory;
        this.versionNumber = versionNumber;
    }

    public SecondCategoryEntity(String koreanName, String englishName, IndustryCategoryEntity industryCategory, FirstCategoryEntity firstCategory) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.industryCategory = industryCategory;
        this.firstCategory = firstCategory;
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

    public void updateFirstCategory(FirstCategoryEntity firstCategory) {
        this.firstCategory = firstCategory;
    }
}

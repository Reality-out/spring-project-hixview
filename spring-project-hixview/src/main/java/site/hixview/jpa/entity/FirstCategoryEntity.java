package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = FIRST_CATEGORY_SNAKE)
@Getter
public class FirstCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = INDU_CATE_NUM_SNAKE)
    private IndustryCategoryEntity industryCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FirstCategoryEntity firstCategory = (FirstCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getKorName(), firstCategory.getKorName())
                .append(getEngName(), firstCategory.getEngName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKorName())
                .append(getEngName())
                .toHashCode();
    }

    public FirstCategoryEntity(String korName, String engName, IndustryCategoryEntity industryCategory) {
        this.korName = korName;
        this.engName = engName;
        this.industryCategory = industryCategory;
    }

    public void updateKorName(String korName) {
        this.korName = korName;
    }

    public void updateEngName(String engName) {
        this.engName = engName;
    }

    public void updateIndustryCategory(IndustryCategoryEntity industryCategory) {
        this.industryCategory = industryCategory;
    }
}

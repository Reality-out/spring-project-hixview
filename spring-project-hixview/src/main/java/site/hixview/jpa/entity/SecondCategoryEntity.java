package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = SECOND_CATEGORY_SNAKE)
@Getter
public class SecondCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Long number;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String korName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String engName;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = INDU_CATE_NUM_SNAKE)
    private IndustryCategoryEntity industryCategory;

    @Column(nullable = false)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = FIR_CATE_NUM_SNAKE)
    private FirstCategoryEntity firstCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SecondCategoryEntity secondCategory = (SecondCategoryEntity) obj;
        return new EqualsBuilder()
                .append(getKorName(), secondCategory.getKorName())
                .append(getEngName(), secondCategory.getEngName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getKorName())
                .append(getEngName())
                .toHashCode();
    }

    public SecondCategoryEntity(String korName, String engName, IndustryCategoryEntity industryCategory, FirstCategoryEntity firstCategory) {
        this.korName = korName;
        this.engName = engName;
        this.industryCategory = industryCategory;
        this.firstCategory = firstCategory;
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

    public void updateFirstCategory(FirstCategoryEntity firstCategory) {
        this.firstCategory = firstCategory;
    }
}

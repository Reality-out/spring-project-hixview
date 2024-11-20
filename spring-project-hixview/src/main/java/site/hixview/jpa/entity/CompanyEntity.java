package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.COMPANY;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY)
@Getter
@Builder(access = AccessLevel.PUBLIC)
public class CompanyEntity {
    @Id
    @Column(nullable = false)
    private String code;

    @Column(name = KOR_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String koreanName;

    @Column(name = ENG_NAME_SNAKE, unique = true, length = 80, nullable = false)
    private String englishName;

    @Column(name = NAME_LISTED_SNAKE, length = 80, nullable = false)
    private String nameListed;

    @Column(name = COUNTRY_LISTED_SNAKE, length = 80, nullable = false)
    private String countryListed;

    @Column(length = 80, nullable = false)
    private String scale;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = FIR_CATE_NUM_SNAKE, nullable = false)
    private FirstCategoryEntity firstCategory;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = SEC_CATE_NUM_SNAKE, nullable = false)
    private SecondCategoryEntity secondCategory;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyEntity company = (CompanyEntity) obj;
        return new EqualsBuilder().append(getCode(), company.getCode()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getCode()).toHashCode();
    }
}

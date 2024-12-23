package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.COMPANY;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(length = 10, nullable = false)
    private String scale;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = FIR_CATE_NUM_SNAKE, nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private FirstCategoryEntity firstCategory;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = SEC_CATE_NUM_SNAKE, nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private SecondCategoryEntity secondCategory;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

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

    public void updateFirstCategory(FirstCategoryEntity firstCategory) {
        this.firstCategory = firstCategory;
    }

    public void updateSecondCategory(SecondCategoryEntity secondCategory) {
        this.secondCategory = secondCategory;
    }

    public static CompanyEntityBuilder builder() {
        return new CompanyEntityBuilder();
    }

    private CompanyEntity(String code, String koreanName, String englishName, String nameListed, String countryListed, String scale, FirstCategoryEntity firstCategory, SecondCategoryEntity secondCategory) {
        this.code = code;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.nameListed = nameListed;
        this.countryListed = countryListed;
        this.scale = scale;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
    }

    public static final class CompanyEntityBuilder {
        private String code;
        private String koreanName;
        private String englishName;
        private String nameListed;
        private String countryListed;
        private String scale;
        private FirstCategoryEntity firstCategory;
        private SecondCategoryEntity secondCategory;

        public CompanyEntityBuilder code(final String code) {
            this.code = code;
            return this;
        }

        public CompanyEntityBuilder koreanName(final String koreanName) {
            this.koreanName = koreanName;
            return this;
        }

        public CompanyEntityBuilder englishName(final String englishName) {
            this.englishName = englishName;
            return this;
        }

        public CompanyEntityBuilder nameListed(final String nameListed) {
            this.nameListed = nameListed;
            return this;
        }

        public CompanyEntityBuilder countryListed(final String countryListed) {
            this.countryListed = countryListed;
            return this;
        }

        public CompanyEntityBuilder scale(final String scale) {
            this.scale = scale;
            return this;
        }

        public CompanyEntityBuilder firstCategory(final FirstCategoryEntity firstCategory) {
            this.firstCategory = firstCategory;
            return this;
        }

        public CompanyEntityBuilder secondCategory(final SecondCategoryEntity secondCategory) {
            this.secondCategory = secondCategory;
            return this;
        }

        public CompanyEntityBuilder company(final CompanyEntity company) {
            this.code = company.getCode();
            this.koreanName = company.getKoreanName();
            this.englishName = company.getEnglishName();
            this.nameListed = company.getNameListed();
            this.countryListed = company.getCountryListed();
            this.scale = company.getScale();
            this.firstCategory = company.getFirstCategory();
            this.secondCategory = company.getSecondCategory();
            return this;
        }

        public CompanyEntity build() {
            return new CompanyEntity(this.code, this.koreanName, this.englishName, this.nameListed, this.countryListed, this.scale, this.firstCategory, this.secondCategory);
        }
    }
}

package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import site.hixview.jpa.entity.type.MapperTable;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMP_ARTI_COMP_MAPPER_SNAKE)
@MapperTable
@Getter
@NoArgsConstructor
public class CompanyArticleCompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CompanyArticleEntity companyArticle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = COMP_CODE_SNAKE, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CompanyEntity company;

    @Version
    @Column(name = VERSION_NUM_SNAKE, nullable = false)
    private Long versionNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyArticleCompanyEntity companyArticleCompanyMapper = (CompanyArticleCompanyEntity) obj;
        return new EqualsBuilder()
                .append(getCompanyArticle().getName(), companyArticleCompanyMapper.getCompanyArticle().getName())
                .append(getCompany().getCode(), companyArticleCompanyMapper.getCompany().getCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCompanyArticle().getName())
                .append(getCompany().getCode())
                .toHashCode();
    }

    public CompanyArticleCompanyEntity(Long number, CompanyArticleEntity companyArticle, CompanyEntity company) {
        this.number = number;
        this.companyArticle = companyArticle;
        this.company = company;
    }

    public CompanyArticleCompanyEntity(CompanyArticleEntity companyArticle, CompanyEntity company, Long versionNumber) {
        this.companyArticle = companyArticle;
        this.company = company;
        this.versionNumber = versionNumber;
    }

    public CompanyArticleCompanyEntity(CompanyArticleEntity companyArticle, CompanyEntity company) {
        this.companyArticle = companyArticle;
        this.company = company;
    }

    public void updateArticle(CompanyArticleEntity article) {
        this.companyArticle = article;
    }

    public void updateCompany(CompanyEntity company) {
        this.company = company;
    }
}

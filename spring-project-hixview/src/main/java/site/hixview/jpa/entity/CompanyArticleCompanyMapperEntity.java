package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMP_ARTI_COMP_MAPPER_SNAKE)
@Getter
@NoArgsConstructor
public class CompanyArticleCompanyMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private CompanyArticleEntity companyArticle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional = false)
    @JoinColumn(name = COMP_CODE_SNAKE)
    private CompanyEntity company;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyArticleCompanyMapperEntity companyArticleCompanyMapper = (CompanyArticleCompanyMapperEntity) obj;
        return new EqualsBuilder()
                .append(getCompanyArticle().getArticle().getName(), companyArticleCompanyMapper.getCompanyArticle().getArticle().getName())
                .append(getCompany().getCode(), companyArticleCompanyMapper.getCompany().getCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getCompanyArticle().getArticle().getName())
                .append(getCompany().getCode())
                .toHashCode();
    }

    public CompanyArticleCompanyMapperEntity(CompanyArticleEntity companyArticle, CompanyEntity company) {
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

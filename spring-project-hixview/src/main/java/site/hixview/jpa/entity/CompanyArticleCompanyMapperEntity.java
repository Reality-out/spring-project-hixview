package site.hixview.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static site.hixview.aggregate.vo.WordCamel.NUM;
import static site.hixview.aggregate.vo.WordSnake.*;

@Entity
@Table(name = COMPANY_ARTICLE_COMPANY_MAPPER_SNAKE)
@Getter
public class CompanyArticleCompanyMapperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NUM, nullable = false)
    private Long number;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = ARTI_NUM_SNAKE)
    private ArticleEntity article;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @MapsId
    @JoinColumn(name = COMP_CODE_SNAKE)
    private CompanyEntity company;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CompanyArticleCompanyMapperEntity companyArticleCompanyMapper = (CompanyArticleCompanyMapperEntity) obj;
        return new EqualsBuilder()
                .append(getArticle().getName(), companyArticleCompanyMapper.getArticle().getName())
                .append(getCompany().getCode(), companyArticleCompanyMapper.getCompany().getCode())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getArticle().getName())
                .append(getCompany().getCode())
                .toHashCode();
    }

    public CompanyArticleCompanyMapperEntity(ArticleEntity article, CompanyEntity company) {
        this.article = article;
        this.company = company;
    }

    public void updateArticle(ArticleEntity article) {
        this.article = article;
    }

    public void updateCompany(CompanyEntity company) {
        this.company = company;
    }
}

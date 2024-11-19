package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.CompanyArticleCompanyMapperEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

public interface CompanyArticleCompanyMapperRepository extends BasicMapperRepositoryFunction<CompanyArticleEntity, CompanyEntity>, JpaRepository<CompanyArticleCompanyMapperEntity, Long> {
    /**
     * SELECT CompanyArticleCompanyMapper
     */
    List<CompanyArticleCompanyMapperEntity> findByArticle(ArticleEntity article);

    List<CompanyArticleCompanyMapperEntity> findByCompany(CompanyEntity company);
}

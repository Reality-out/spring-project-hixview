package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.CompanyArticleCompanyMapperEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface CompanyArticleCompanyMapperRepository extends BasicMapperRepositoryFunction<CompanyArticleEntity, CompanyEntity>, JpaRepository<CompanyArticleCompanyMapperEntity, Long> {
    /**
     * SELECT CompanyArticleCompanyMapper
     */
    List<CompanyArticleCompanyMapperEntity> findByArticle(ArticleEntity article);

    List<CompanyArticleCompanyMapperEntity> findByCompany(CompanyEntity company);
}

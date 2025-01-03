package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.method.BasicMapperEntityRepositoryFunction;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyArticleCompanyEntityRepository extends BasicMapperEntityRepositoryFunction<CompanyArticleCompanyEntity, CompanyArticleEntity, CompanyEntity>, JpaRepository<CompanyArticleCompanyEntity, Long> {
    /**
     * SELECT CompanyArticleCompanyMapper
     */
    List<CompanyArticleCompanyEntity> findByCompanyArticle(CompanyArticleEntity companyArticle);

    List<CompanyArticleCompanyEntity> findByCompany(CompanyEntity company);

    Optional<CompanyArticleCompanyEntity> findByCompanyArticleAndCompany(CompanyArticleEntity companyArticle, CompanyEntity company);
}

package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface CompanyArticleCompanyRepository extends BasicMapperRepositoryFunction<CompanyArticleCompanyEntity, CompanyArticleEntity, CompanyEntity>, JpaRepository<CompanyArticleCompanyEntity, Long> {
    /**
     * SELECT CompanyArticleCompanyMapper
     */
    List<CompanyArticleCompanyEntity> findByCompanyArticle(CompanyArticleEntity article);

    List<CompanyArticleCompanyEntity> findByCompany(CompanyEntity company);
}

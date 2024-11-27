package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.CompanyArticleCompanyMapperEntity;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface CompanyArticleCompanyMapperRepository extends BasicMapperRepositoryFunction<CompanyArticleCompanyMapperEntity, CompanyArticleEntity, CompanyEntity>, JpaRepository<CompanyArticleCompanyMapperEntity, Long> {
    /**
     * SELECT CompanyArticleCompanyMapper
     */
    List<CompanyArticleCompanyMapperEntity> findByCompanyArticle(CompanyArticleEntity article);

    List<CompanyArticleCompanyMapperEntity> findByCompany(CompanyEntity company);
}

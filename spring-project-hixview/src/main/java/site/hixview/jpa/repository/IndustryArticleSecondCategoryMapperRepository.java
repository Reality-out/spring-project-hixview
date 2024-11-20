package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryMapperEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface IndustryArticleSecondCategoryMapperRepository extends BasicMapperRepositoryFunction<IndustryArticleSecondCategoryMapperEntity, IndustryArticleEntity, SecondCategoryEntity>, JpaRepository<IndustryArticleSecondCategoryMapperEntity, Long> {
    /**
     * SELECT IndustryArticleSecondCategoryMapper
     */
    List<IndustryArticleSecondCategoryMapperEntity> findByIndustryArticle(IndustryArticleEntity industryArticle);

    List<IndustryArticleSecondCategoryMapperEntity> findBySecondCategory(SecondCategoryEntity secondCategory);
}

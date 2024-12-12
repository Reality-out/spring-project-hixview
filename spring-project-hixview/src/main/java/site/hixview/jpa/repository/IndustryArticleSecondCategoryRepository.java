package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface IndustryArticleSecondCategoryRepository extends BasicMapperRepositoryFunction<IndustryArticleSecondCategoryEntity, IndustryArticleEntity, SecondCategoryEntity>, JpaRepository<IndustryArticleSecondCategoryEntity, Long> {
    /**
     * SELECT IndustryArticleSecondCategoryMapper
     */
    List<IndustryArticleSecondCategoryEntity> findByIndustryArticle(IndustryArticleEntity article);

    List<IndustryArticleSecondCategoryEntity> findBySecondCategory(SecondCategoryEntity secondCategory);

    /**
     * CHECK IndustryArticleSecondCategoryMapper
     */
    boolean existsByNumber(Long number);
}

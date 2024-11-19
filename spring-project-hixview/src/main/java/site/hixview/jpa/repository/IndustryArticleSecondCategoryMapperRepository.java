package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

public interface IndustryArticleSecondCategoryMapperRepository extends BasicMapperRepositoryFunction<IndustryArticleEntity, SecondCategoryEntity>, JpaRepository<IndustryArticleSecondCategoryMapperEntity, Long> {
    /**
     * SELECT IndustryArticleSecondCategoryMapper
     */
    List<IndustryArticleSecondCategoryMapperEntity> findByArticle(ArticleEntity article);

    List<IndustryArticleSecondCategoryMapperEntity> findBySecondCategory(SecondCategoryEntity secondCategory);
}

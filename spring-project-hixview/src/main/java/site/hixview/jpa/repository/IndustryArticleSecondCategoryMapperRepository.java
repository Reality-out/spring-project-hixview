package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryMapperEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface IndustryArticleSecondCategoryMapperRepository extends BasicMapperRepositoryFunction<IndustryArticleEntity, SecondCategoryEntity>, JpaRepository<IndustryArticleSecondCategoryMapperEntity, Long> {
    /**
     * SELECT IndustryArticleSecondCategoryMapper
     */
    List<IndustryArticleSecondCategoryMapperEntity> findByArticle(ArticleEntity article);

    List<IndustryArticleSecondCategoryMapperEntity> findBySecondCategory(SecondCategoryEntity secondCategory);
}

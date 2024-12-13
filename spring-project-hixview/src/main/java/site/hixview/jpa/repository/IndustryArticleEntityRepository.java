package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleEntityRepositoryFunction;

import java.util.List;

@Repository
public interface IndustryArticleEntityRepository extends BasicArticleEntityRepositoryFunction<IndustryArticleEntity>, JpaRepository<IndustryArticleEntity, Long> {
    /**
     * SELECT IndustryArticle
     */
    List<IndustryArticleEntity> findByFirstCategory(FirstCategoryEntity firstCategory);
}

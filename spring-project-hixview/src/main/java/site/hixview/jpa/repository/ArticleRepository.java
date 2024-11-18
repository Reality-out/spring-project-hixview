package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleRepositoryFunction;

public interface ArticleRepository extends BasicArticleRepositoryFunction<ArticleEntity>, JpaRepository<ArticleEntity, Long> {
}

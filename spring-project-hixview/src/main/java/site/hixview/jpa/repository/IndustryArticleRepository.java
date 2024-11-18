package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleRepositoryFunction;

public interface IndustryArticleRepository extends BasicArticleRepositoryFunction<IndustryArticleEntity>, JpaRepository<IndustryArticleEntity, Long> {
}

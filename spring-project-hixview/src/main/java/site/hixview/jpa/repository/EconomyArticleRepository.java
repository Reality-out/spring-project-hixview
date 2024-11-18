package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleRepositoryFunction;

public interface EconomyArticleRepository extends BasicArticleRepositoryFunction<EconomyArticleEntity>, JpaRepository<EconomyArticleEntity, Long> {
}

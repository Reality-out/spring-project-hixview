package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.repository.method.BasicArticleEntityRepositoryFunction;

@Repository
public interface EconomyArticleEntityRepository extends BasicArticleEntityRepositoryFunction<EconomyArticleEntity>, JpaRepository<EconomyArticleEntity, Long> {
}

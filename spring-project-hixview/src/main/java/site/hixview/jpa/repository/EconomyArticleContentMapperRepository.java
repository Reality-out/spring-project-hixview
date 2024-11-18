package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.EconomyArticleContentMapperEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

public interface EconomyArticleContentMapperRepository extends BasicMapperRepositoryFunction<EconomyArticleEntity, EconomyContentEntity>, JpaRepository<EconomyArticleContentMapperEntity, Long> {
}

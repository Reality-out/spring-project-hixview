package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.method.BasicMapperEntityRepositoryFunction;

import java.util.List;
import java.util.Optional;

@Repository
public interface EconomyArticleContentEntityRepository extends BasicMapperEntityRepositoryFunction<EconomyArticleContentEntity, EconomyArticleEntity, EconomyContentEntity>, JpaRepository<EconomyArticleContentEntity, Long> {
    /**
     * SELECT EconomyArticleContentMapper
     */
    List<EconomyArticleContentEntity> findByEconomyArticle(EconomyArticleEntity economyArticle);

    List<EconomyArticleContentEntity> findByEconomyContent(EconomyContentEntity economyContent);

    Optional<EconomyArticleContentEntity> findByEconomyArticleAndEconomyContent(EconomyArticleEntity economyArticle, EconomyContentEntity economyContent);
}

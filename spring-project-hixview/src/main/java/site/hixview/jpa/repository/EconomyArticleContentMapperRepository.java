package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hixview.jpa.entity.*;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

public interface EconomyArticleContentMapperRepository extends BasicMapperRepositoryFunction<EconomyArticleEntity, EconomyContentEntity>, JpaRepository<EconomyArticleContentMapperEntity, Long> {
    /**
     * SELECT EconomyArticleContentMapper
     */
    List<EconomyArticleContentMapperEntity> findByArticle(ArticleEntity article);

    List<EconomyArticleContentMapperEntity> findByEconomyContent(EconomyContentEntity economyContent);

}

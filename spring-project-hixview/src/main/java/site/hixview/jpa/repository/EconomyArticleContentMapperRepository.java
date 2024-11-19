package site.hixview.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleContentMapperEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.method.BasicMapperRepositoryFunction;

import java.util.List;

@Repository
public interface EconomyArticleContentMapperRepository extends BasicMapperRepositoryFunction<EconomyArticleEntity, EconomyContentEntity>, JpaRepository<EconomyArticleContentMapperEntity, Long> {
    /**
     * SELECT EconomyArticleContentMapper
     */
    List<EconomyArticleContentMapperEntity> findByArticle(ArticleEntity article);

    List<EconomyArticleContentMapperEntity> findByEconomyContent(EconomyContentEntity economyContent);

}

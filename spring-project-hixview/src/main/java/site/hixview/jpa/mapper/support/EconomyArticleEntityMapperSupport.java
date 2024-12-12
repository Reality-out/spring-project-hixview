package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.domain.EconomyArticle.EconomyArticleBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity.EconomyArticleEntityBuilder;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.repository.ArticleRepository;
import site.hixview.jpa.repository.EconomyArticleContentRepository;
import site.hixview.jpa.repository.PressRepository;

public abstract class EconomyArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private PressRepository pressRepository;

    @Autowired
    private EconomyArticleContentRepository economyArticleContentRepository;

    @AfterMapping
    public EconomyArticleEntityBuilder afterMappingToEntity(
            @MappingTarget EconomyArticleEntityBuilder builder, EconomyArticle economyArticle) {
        return builder.article(articleRepository.findByNumber(economyArticle.getNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(economyArticle.getNumber(), ArticleEntity.class)))
                .press(pressRepository.findByNumber(economyArticle.getPressNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(economyArticle.getPressNumber(), PressEntity.class)));
    }

    @AfterMapping
    public EconomyArticleBuilder afterMappingToDomain(
            @MappingTarget EconomyArticleBuilder builder, EconomyArticleEntity entity) {
        return builder.mappedEconomyContentNumbers(economyArticleContentRepository.findByEconomyArticle(entity)
                .stream().map(data -> data.getEconomyContent().getNumber()).toList());
    }
}

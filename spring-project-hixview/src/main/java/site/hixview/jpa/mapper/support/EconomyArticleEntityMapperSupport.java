package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.domain.EconomyArticle.EconomyArticleBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyArticleEntity.EconomyArticleEntityBuilder;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

public interface EconomyArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget EconomyArticleEntityBuilder builder, EconomyArticle economyArticle,
            @Context ArticleEntityRepository articleEntityRepository,
            @Context PressEntityRepository pressEntityRepository) {
        Long articleNumber = economyArticle.getNumber();
        Long pressNumber = economyArticle.getPressNumber();
        builder.article(articleEntityRepository.findByNumber(articleNumber).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(articleNumber, ArticleEntity.class)))
                .press(pressEntityRepository.findByNumber(pressNumber).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(pressNumber, PressEntity.class)));
    }

    @AfterMapping
    default void afterMappingToDomain(
            @MappingTarget EconomyArticleBuilder builder, EconomyArticleEntity entity,
            @Context EconomyArticleContentEntityRepository economyArticleContentRepository) {
        builder.mappedEconomyContentNumbers(economyArticleContentRepository.findByEconomyArticle(entity)
                .stream().map(data -> data.getEconomyContent().getNumber()).toList());
    }
}

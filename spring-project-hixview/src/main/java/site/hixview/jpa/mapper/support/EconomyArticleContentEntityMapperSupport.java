package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

public interface EconomyArticleContentEntityMapperSupport {
    @AfterMapping
    default EconomyArticleContentEntity afterMappingToEntity(
            @MappingTarget EconomyArticleContentEntity ignoredEntity, EconomyArticleContent economyArticleContent,
            @Context EconomyArticleEntityRepository economyArticleRepository,
            @Context EconomyContentEntityRepository economyContentEntityRepository) {
        return new EconomyArticleContentEntity(economyArticleContent.getNumber(),
                economyArticleRepository.findByNumber(economyArticleContent.getArticleNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                economyArticleContent.getArticleNumber(), EconomyArticleEntity.class)),
                economyContentEntityRepository.findByNumber(economyArticleContent.getContentNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                economyArticleContent.getContentNumber(), EconomyContentEntity.class)));
    }

    @Named("articleNumberToDomain")
    default Long articleNumberToDomain(EconomyArticleEntity economyArticle) {
        return economyArticle.getNumber();
    }

    @Named("contentNumberToDomain")
    default Long contentNumberToDomain(EconomyContentEntity economyContent) {
        return economyContent.getNumber();
    }
}

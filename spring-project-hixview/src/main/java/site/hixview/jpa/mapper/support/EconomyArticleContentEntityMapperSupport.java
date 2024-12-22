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
        Long articleNumber = economyArticleContent.getArticleNumber();
        Long contentNumber = economyArticleContent.getContentNumber();
        return new EconomyArticleContentEntity(economyArticleContent.getNumber(),
                economyArticleRepository.findByNumber(articleNumber)
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                articleNumber, EconomyArticleEntity.class)),
                economyContentEntityRepository.findByNumber(contentNumber)
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                contentNumber, EconomyContentEntity.class)));
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

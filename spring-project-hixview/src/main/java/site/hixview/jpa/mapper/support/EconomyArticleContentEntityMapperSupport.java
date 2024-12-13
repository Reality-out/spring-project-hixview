package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.entity.EconomyContentEntity;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

public abstract class EconomyArticleContentEntityMapperSupport {
    @Autowired
    private EconomyArticleEntityRepository economyArticleRepository;

    @Autowired
    private EconomyContentEntityRepository economyContentEntityRepository;

    @AfterMapping
    public void afterMappingToEntity(
            @MappingTarget EconomyArticleContentEntity entity, EconomyArticleContent economyArticleContent) {
        entity.updateArticle(economyArticleRepository.findByNumber(economyArticleContent.getArticleNumber())
                .orElseThrow(() -> new EntityNotFoundWithNumberException(economyArticleContent.getArticleNumber(),
                        EconomyArticleEntity.class)));
        entity.updateEconomyContent(economyContentEntityRepository.findByNumber(economyArticleContent.getContentNumber())
                .orElseThrow(() -> new EntityNotFoundWithNumberException(economyArticleContent.getContentNumber(),
                        EconomyContentEntity.class)));
    }

    @Named("articleNumberToDomain")
    public Long articleNumberToDomain(EconomyArticleEntity economyArticle) {
        return economyArticle.getNumber();
    }

    @Named("contentNumberToDomain")
    public Long contentNumberToDomain(EconomyContentEntity economyContent) {
        return economyContent.getNumber();
    }
}

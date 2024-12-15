package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.domain.IndustryArticle.IndustryArticleBuilder;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.ArticleEntity;
import site.hixview.jpa.entity.FirstCategoryEntity;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleEntity.IndustryArticleEntityBuilder;
import site.hixview.jpa.entity.PressEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryArticleSecondCategoryEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

public interface IndustryArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @AfterMapping
    default void afterMappingToEntity(
            @MappingTarget IndustryArticleEntityBuilder builder, IndustryArticle industryArticle,
            @Context ArticleEntityRepository articleEntityRepository,
            @Context PressEntityRepository pressEntityRepository,
            @Context FirstCategoryEntityRepository firstCategoryRepository) {
        builder.press(pressEntityRepository.findByNumber(industryArticle.getPressNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(industryArticle.getPressNumber(), PressEntity.class)))
                .article(articleEntityRepository.findByNumber(industryArticle.getNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(industryArticle.getNumber(), ArticleEntity.class)))
                .firstCategory(firstCategoryRepository.findByNumber(industryArticle.getFirstCategoryNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                industryArticle.getFirstCategoryNumber(), FirstCategoryEntity.class)));
    }

    @Named("firstCategoryNumberToDomain")
    default Long firstCategoryNumberToDomain(FirstCategoryEntity firstCategoryEntity) {
        return firstCategoryEntity.getNumber();
    }

    @AfterMapping
    default void afterMappingToDomain(
            @MappingTarget IndustryArticleBuilder builder, IndustryArticleEntity entity,
            @Context IndustryArticleSecondCategoryEntityRepository industryArticleSecondCategoryRepository) {
        builder.mappedSecondCategoryNumbers(
                industryArticleSecondCategoryRepository.findByIndustryArticle(entity).stream()
                        .map(data -> data.getSecondCategory().getNumber()).toList());
    }
}

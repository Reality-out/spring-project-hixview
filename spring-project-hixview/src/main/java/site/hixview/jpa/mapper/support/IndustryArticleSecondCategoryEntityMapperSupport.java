package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

public interface IndustryArticleSecondCategoryEntityMapperSupport {
    @AfterMapping
    default IndustryArticleSecondCategoryEntity afterMappingToEntity(
            @MappingTarget IndustryArticleSecondCategoryEntity ignoredEntity,
            IndustryArticleSecondCategory industryArticleSecondCategory,
            @Context IndustryArticleEntityRepository industryArticleRepository,
            @Context SecondCategoryEntityRepository secondCategoryRepository) {
        Long articleNumber = industryArticleSecondCategory.getArticleNumber();
        Long secondCategoryNumber = industryArticleSecondCategory.getSecondCategoryNumber();
        return new IndustryArticleSecondCategoryEntity(industryArticleSecondCategory.getNumber(),
                industryArticleRepository.findByNumber(articleNumber).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(articleNumber, IndustryArticleEntity.class)),
                secondCategoryRepository.findByNumber(secondCategoryNumber).orElseThrow(
                        () -> new EntityNotFoundWithNumberException(secondCategoryNumber, SecondCategoryEntity.class)));
    }

    @Named("articleNumberToDomain")
    default Long articleNumberToDomain(IndustryArticleEntity industryArticleEntity) {
        return industryArticleEntity.getNumber();
    }

    @Named("secondCategoryNumberToDomain")
    default Long secondCategoryNumberToDomain(SecondCategoryEntity secondCategoryEntity) {
        return secondCategoryEntity.getNumber();
    }
}

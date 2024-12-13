package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import site.hixview.aggregate.error.EntityNotFoundWithNumberException;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

public abstract class IndustryArticleSecondCategoryEntityMapperSupport {
    @Autowired
    private IndustryArticleEntityRepository industryArticleRepository;

    @Autowired
    private SecondCategoryEntityRepository secondCategoryRepository;

    @AfterMapping
    public void afterMappingToEntity(@MappingTarget IndustryArticleSecondCategoryEntity entity, Long articleNumber, Long secondCategoryNumber) {
        entity.updateArticle(industryArticleRepository.findByNumber(articleNumber).orElseThrow(() ->
                new EntityNotFoundWithNumberException(articleNumber, IndustryArticleEntity.class)));
        entity.updateSecondCategory(secondCategoryRepository.findByNumber(secondCategoryNumber).orElseThrow(
                () -> new EntityNotFoundWithNumberException(secondCategoryNumber, SecondCategoryEntity.class)));
    }

    @Named("articleNumberToDomain")
    public Long articleNumberToDomain(IndustryArticleEntity industryArticleEntity) {
        return industryArticleEntity.getNumber();
    }

    @Named("secondCategoryNumberToDomain")
    public Long secondCategoryNumberToDomain(SecondCategoryEntity secondCategoryEntity) {
        return secondCategoryEntity.getNumber();
    }
}

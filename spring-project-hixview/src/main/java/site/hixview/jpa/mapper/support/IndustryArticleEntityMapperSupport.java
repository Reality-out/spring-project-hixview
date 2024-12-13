package site.hixview.jpa.mapper.support;

import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
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

public abstract class IndustryArticleEntityMapperSupport extends SuperArticleEntityMapperSupport {
    @Autowired
    private ArticleEntityRepository articleEntityRepository;

    @Autowired
    private PressEntityRepository pressEntityRepository;

    @Autowired
    private FirstCategoryEntityRepository firstCategoryRepository;

    @Autowired
    private IndustryArticleSecondCategoryEntityRepository industryArticleSecondCategoryRepository;

    @AfterMapping
    public IndustryArticleEntityBuilder afterMappingToEntity(
            @MappingTarget IndustryArticleEntityBuilder builder, IndustryArticle industryArticle) {
        return builder.article(articleEntityRepository.findByNumber(industryArticle.getNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(industryArticle.getNumber(), ArticleEntity.class)))
                .press(pressEntityRepository.findByNumber(industryArticle.getPressNumber()).orElseThrow(() ->
                        new EntityNotFoundWithNumberException(industryArticle.getPressNumber(), PressEntity.class)))
                .firstCategory(firstCategoryRepository.findByNumber(industryArticle.getFirstCategoryNumber())
                        .orElseThrow(() -> new EntityNotFoundWithNumberException(
                                industryArticle.getFirstCategoryNumber(), FirstCategoryEntity.class)));
    }

    @Named("firstCategoryNumberToDomain")
    public Long firstCategoryNumberToDomain(FirstCategoryEntity firstCategoryEntity) {
        return firstCategoryEntity.getNumber();
    }

    @AfterMapping
    public IndustryArticleBuilder afterMappingToDomain(
            @MappingTarget IndustryArticleBuilder builder, IndustryArticleEntity entity) {
        return builder.mappedSecondCategoryNumbers(
                industryArticleSecondCategoryRepository.findByIndustryArticle(entity).stream()
                        .map(data -> data.getSecondCategory().getNumber()).toList());
    }
}

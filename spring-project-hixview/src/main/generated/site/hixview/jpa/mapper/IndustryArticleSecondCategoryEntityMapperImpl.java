package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:24:27+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryArticleSecondCategoryEntityMapperImpl extends IndustryArticleSecondCategoryEntityMapper {

    @Override
    public IndustryArticleSecondCategoryEntity toIndustryArticleSecondCategoryEntity(IndustryArticleSecondCategory industryArticleSecondCategory) {
        if ( industryArticleSecondCategory == null ) {
            return null;
        }

        IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity = new IndustryArticleSecondCategoryEntity();

        return industryArticleSecondCategoryEntity;
    }

    @Override
    public IndustryArticleSecondCategory toIndustryArticleSecondCategory(IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity) {
        if ( industryArticleSecondCategoryEntity == null ) {
            return null;
        }

        IndustryArticleSecondCategory.IndustryArticleSecondCategoryBuilder industryArticleSecondCategory = IndustryArticleSecondCategory.builder();

        industryArticleSecondCategory.articleNumber( articleNumberToDomain( industryArticleSecondCategoryEntity.getIndustryArticle() ) );
        industryArticleSecondCategory.secondCategoryNumber( secondCategoryNumberToDomain( industryArticleSecondCategoryEntity.getSecondCategory() ) );
        industryArticleSecondCategory.number( industryArticleSecondCategoryEntity.getNumber() );

        return industryArticleSecondCategory.build();
    }
}

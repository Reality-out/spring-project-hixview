package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryArticleSecondCategory;
import site.hixview.jpa.entity.IndustryArticleSecondCategoryEntity;
import site.hixview.jpa.repository.IndustryArticleEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryArticleSecondCategoryEntityMapperImpl implements IndustryArticleSecondCategoryEntityMapper {

    @Override
    public IndustryArticleSecondCategoryEntity toIndustryArticleSecondCategoryEntity(IndustryArticleSecondCategory industryArticleSecondCategory, IndustryArticleEntityRepository industryArticleRepository, SecondCategoryEntityRepository secondCategoryRepository) {
        if ( industryArticleSecondCategory == null ) {
            return null;
        }

        IndustryArticleSecondCategoryEntity industryArticleSecondCategoryEntity = new IndustryArticleSecondCategoryEntity();

        afterMappingToEntity( industryArticleSecondCategoryEntity, industryArticleSecondCategory, industryArticleRepository, secondCategoryRepository );

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

package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.IndustryArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryArticleSecondCategoryEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryArticleEntityMapperImpl implements IndustryArticleEntityMapper {

    @Override
    public IndustryArticleEntity toIndustryArticleEntity(IndustryArticle industryArticle, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository, FirstCategoryEntityRepository firstCategoryRepository) {
        if ( industryArticle == null ) {
            return null;
        }

        IndustryArticleEntity.IndustryArticleEntityBuilder industryArticleEntity = IndustryArticleEntity.builder();

        industryArticleEntity.name( industryArticle.getName() );
        industryArticleEntity.link( industryArticle.getLink() );
        industryArticleEntity.date( industryArticle.getDate() );
        if ( industryArticle.getSubjectCountry() != null ) {
            industryArticleEntity.subjectCountry( industryArticle.getSubjectCountry().name() );
        }
        if ( industryArticle.getImportance() != null ) {
            industryArticleEntity.importance( industryArticle.getImportance().name() );
        }
        industryArticleEntity.summary( industryArticle.getSummary() );

        return industryArticleEntity.build();
    }

    @Override
    public IndustryArticle toIndustryArticle(IndustryArticleEntity industryArticleEntity, IndustryArticleSecondCategoryEntityRepository industryArticleSecondCategoryRepository) {
        if ( industryArticleEntity == null ) {
            return null;
        }

        IndustryArticle.IndustryArticleBuilder industryArticle = IndustryArticle.builder();

        industryArticle.number( numberToDomain( industryArticleEntity.getArticle() ) );
        industryArticle.pressNumber( pressNumberToDomain( industryArticleEntity.getPress() ) );
        industryArticle.firstCategoryNumber( firstCategoryNumberToDomain( industryArticleEntity.getFirstCategory() ) );
        industryArticle.name( industryArticleEntity.getName() );
        industryArticle.link( industryArticleEntity.getLink() );
        industryArticle.date( industryArticleEntity.getDate() );
        if ( industryArticleEntity.getSubjectCountry() != null ) {
            industryArticle.subjectCountry( Enum.valueOf( Country.class, industryArticleEntity.getSubjectCountry() ) );
        }
        if ( industryArticleEntity.getImportance() != null ) {
            industryArticle.importance( Enum.valueOf( Importance.class, industryArticleEntity.getImportance() ) );
        }
        industryArticle.summary( industryArticleEntity.getSummary() );

        return industryArticle.build();
    }
}

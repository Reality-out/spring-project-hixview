package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.EconomyArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.EconomyArticleContentEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyArticleEntityMapperImpl implements EconomyArticleEntityMapper {

    @Override
    public EconomyArticleEntity toEconomyArticleEntity(EconomyArticle economyArticle, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository) {
        if ( economyArticle == null ) {
            return null;
        }

        EconomyArticleEntity.EconomyArticleEntityBuilder economyArticleEntity = EconomyArticleEntity.builder();

        economyArticleEntity.name( economyArticle.getName() );
        economyArticleEntity.link( economyArticle.getLink() );
        economyArticleEntity.date( economyArticle.getDate() );
        if ( economyArticle.getSubjectCountry() != null ) {
            economyArticleEntity.subjectCountry( economyArticle.getSubjectCountry().name() );
        }
        if ( economyArticle.getImportance() != null ) {
            economyArticleEntity.importance( economyArticle.getImportance().name() );
        }
        economyArticleEntity.summary( economyArticle.getSummary() );

        return economyArticleEntity.build();
    }

    @Override
    public EconomyArticle toEconomyArticle(EconomyArticleEntity economyArticleEntity, EconomyArticleContentEntityRepository economyArticleContentRepository) {
        if ( economyArticleEntity == null ) {
            return null;
        }

        EconomyArticle.EconomyArticleBuilder economyArticle = EconomyArticle.builder();

        economyArticle.number( numberToDomain( economyArticleEntity.getArticle() ) );
        economyArticle.pressNumber( pressNumberToDomain( economyArticleEntity.getPress() ) );
        economyArticle.name( economyArticleEntity.getName() );
        economyArticle.link( economyArticleEntity.getLink() );
        economyArticle.date( economyArticleEntity.getDate() );
        if ( economyArticleEntity.getSubjectCountry() != null ) {
            economyArticle.subjectCountry( Enum.valueOf( Country.class, economyArticleEntity.getSubjectCountry() ) );
        }
        if ( economyArticleEntity.getImportance() != null ) {
            economyArticle.importance( Enum.valueOf( Importance.class, economyArticleEntity.getImportance() ) );
        }
        economyArticle.summary( economyArticleEntity.getSummary() );

        return economyArticle.build();
    }
}

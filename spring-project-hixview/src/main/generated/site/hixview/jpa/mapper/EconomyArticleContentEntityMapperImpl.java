package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.jpa.entity.EconomyArticleContentEntity;
import site.hixview.jpa.repository.EconomyArticleEntityRepository;
import site.hixview.jpa.repository.EconomyContentEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyArticleContentEntityMapperImpl implements EconomyArticleContentEntityMapper {

    @Override
    public EconomyArticleContentEntity toEconomyArticleContentEntity(EconomyArticleContent economyArticleContent, EconomyArticleEntityRepository economyArticleRepository, EconomyContentEntityRepository economyContentEntityRepository) {
        if ( economyArticleContent == null ) {
            return null;
        }

        EconomyArticleContentEntity economyArticleContentEntity = new EconomyArticleContentEntity();

        afterMappingToEntity( economyArticleContentEntity, economyArticleContent, economyArticleRepository, economyContentEntityRepository );

        return economyArticleContentEntity;
    }

    @Override
    public EconomyArticleContent toEconomyArticleContent(EconomyArticleContentEntity economyArticleContentEntity) {
        if ( economyArticleContentEntity == null ) {
            return null;
        }

        EconomyArticleContent.EconomyArticleContentBuilder economyArticleContent = EconomyArticleContent.builder();

        economyArticleContent.articleNumber( articleNumberToDomain( economyArticleContentEntity.getEconomyArticle() ) );
        economyArticleContent.contentNumber( contentNumberToDomain( economyArticleContentEntity.getEconomyContent() ) );
        economyArticleContent.number( economyArticleContentEntity.getNumber() );

        return economyArticleContent.build();
    }
}

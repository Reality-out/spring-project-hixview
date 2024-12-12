package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.jpa.entity.EconomyArticleContentEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:16:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyArticleContentEntityMapperImpl extends EconomyArticleContentEntityMapper {

    @Override
    public EconomyArticleContentEntity toEconomyArticleContentEntity(EconomyArticleContent economyArticleContent) {
        if ( economyArticleContent == null ) {
            return null;
        }

        EconomyArticleContentEntity economyArticleContentEntity = new EconomyArticleContentEntity();

        afterMappingToEntity( economyArticleContentEntity, economyArticleContent );

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

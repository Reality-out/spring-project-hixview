package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyArticleContent;
import site.hixview.aggregate.dto.EconomyArticleContentDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T11:41:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyArticleContentMapperImpl implements EconomyArticleContentMapper {

    @Override
    public EconomyArticleContent toEconomyArticleContent(EconomyArticleContentDto economyArticleContentDto) {
        if ( economyArticleContentDto == null ) {
            return null;
        }

        EconomyArticleContent.EconomyArticleContentBuilder economyArticleContent = EconomyArticleContent.builder();

        economyArticleContent.number( economyArticleContentDto.getNumber() );
        economyArticleContent.articleNumber( economyArticleContentDto.getArticleNumber() );
        economyArticleContent.contentNumber( economyArticleContentDto.getContentNumber() );

        return economyArticleContent.build();
    }

    @Override
    public EconomyArticleContentDto toEconomyArticleContentDto(EconomyArticleContent economyArticleContent) {
        if ( economyArticleContent == null ) {
            return null;
        }

        EconomyArticleContentDto economyArticleContentDto = new EconomyArticleContentDto();

        economyArticleContentDto.setNumber( economyArticleContent.getNumber() );
        economyArticleContentDto.setArticleNumber( economyArticleContent.getArticleNumber() );
        economyArticleContentDto.setContentNumber( economyArticleContent.getContentNumber() );

        return economyArticleContentDto;
    }
}

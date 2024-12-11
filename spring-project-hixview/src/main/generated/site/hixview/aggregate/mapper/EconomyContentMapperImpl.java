package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.aggregate.dto.EconomyContentDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-12T00:26:25+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyContentMapperImpl implements EconomyContentMapper {

    @Override
    public EconomyContent toEconomyContent(EconomyContentDto economyContentDto) {
        if ( economyContentDto == null ) {
            return null;
        }

        EconomyContent.EconomyContentBuilder economyContent = EconomyContent.builder();

        economyContent.number( economyContentDto.getNumber() );
        economyContent.name( economyContentDto.getName() );

        return economyContent.build();
    }

    @Override
    public EconomyContentDto toEconomyContentDto(EconomyContent economyContent) {
        if ( economyContent == null ) {
            return null;
        }

        EconomyContentDto economyContentDto = new EconomyContentDto();

        economyContentDto.setNumber( economyContent.getNumber() );
        economyContentDto.setName( economyContent.getName() );

        return economyContentDto;
    }
}

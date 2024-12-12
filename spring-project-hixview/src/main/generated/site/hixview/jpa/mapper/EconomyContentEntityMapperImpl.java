package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyContent;
import site.hixview.jpa.entity.EconomyContentEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:16:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyContentEntityMapperImpl extends EconomyContentEntityMapper {

    @Override
    public EconomyContentEntity toEconomyContentEntity(EconomyContent economyContent) {
        if ( economyContent == null ) {
            return null;
        }

        EconomyContentEntity economyContentEntity = new EconomyContentEntity();

        return economyContentEntity;
    }

    @Override
    public EconomyContent toEconomyContent(EconomyContentEntity economyContentEntity) {
        if ( economyContentEntity == null ) {
            return null;
        }

        EconomyContent.EconomyContentBuilder economyContent = EconomyContent.builder();

        economyContent.number( economyContentEntity.getNumber() );
        economyContent.name( economyContentEntity.getName() );

        return economyContent.build();
    }
}

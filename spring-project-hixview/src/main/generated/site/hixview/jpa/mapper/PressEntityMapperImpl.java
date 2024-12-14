package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Press;
import site.hixview.jpa.entity.PressEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:23:40+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PressEntityMapperImpl implements PressEntityMapper {

    @Override
    public PressEntity toPressEntity(Press press) {
        if ( press == null ) {
            return null;
        }

        PressEntity pressEntity = new PressEntity();

        return pressEntity;
    }

    @Override
    public Press toPress(PressEntity pressEntity) {
        if ( pressEntity == null ) {
            return null;
        }

        Press.PressBuilder press = Press.builder();

        press.number( pressEntity.getNumber() );
        press.koreanName( pressEntity.getKoreanName() );
        press.englishName( pressEntity.getEnglishName() );

        return press.build();
    }
}

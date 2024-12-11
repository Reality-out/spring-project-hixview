package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Press;
import site.hixview.aggregate.dto.PressDto;
import site.hixview.aggregate.dto.PressDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-12T00:26:25+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PressMapperImpl implements PressMapper {

    @Override
    public Press toPress(PressDto pressDto) {
        if ( pressDto == null ) {
            return null;
        }

        Press.PressBuilder press = Press.builder();

        press.number( pressDto.getNumber() );
        press.koreanName( pressDto.getKoreanName() );
        press.englishName( pressDto.getEnglishName() );

        return press.build();
    }

    @Override
    public PressDto toPressDto(Press press) {
        if ( press == null ) {
            return null;
        }

        PressDto pressDto = new PressDto();

        pressDto.setNumber( press.getNumber() );
        pressDto.setKoreanName( press.getKoreanName() );
        pressDto.setEnglishName( press.getEnglishName() );

        return pressDto;
    }

    @Override
    public Press toPress(PressDtoNoNumber pressDto) {
        if ( pressDto == null ) {
            return null;
        }

        Press.PressBuilder press = Press.builder();

        press.koreanName( pressDto.getKoreanName() );
        press.englishName( pressDto.getEnglishName() );

        return press.build();
    }

    @Override
    public PressDtoNoNumber toPressDtoNoNumber(Press press) {
        if ( press == null ) {
            return null;
        }

        PressDtoNoNumber pressDtoNoNumber = new PressDtoNoNumber();

        pressDtoNoNumber.setKoreanName( press.getKoreanName() );
        pressDtoNoNumber.setEnglishName( press.getEnglishName() );

        return pressDtoNoNumber;
    }
}

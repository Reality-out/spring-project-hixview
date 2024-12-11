package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.aggregate.dto.FirstCategoryDto;
import site.hixview.aggregate.dto.FirstCategoryDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-12T00:26:25+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class FirstCategoryMapperImpl implements FirstCategoryMapper {

    @Override
    public FirstCategory toFirstCategory(FirstCategoryDto firstCategoryDto) {
        if ( firstCategoryDto == null ) {
            return null;
        }

        FirstCategory.FirstCategoryBuilder firstCategory = FirstCategory.builder();

        firstCategory.number( firstCategoryDto.getNumber() );
        firstCategory.koreanName( firstCategoryDto.getKoreanName() );
        firstCategory.englishName( firstCategoryDto.getEnglishName() );
        firstCategory.industryCategoryNumber( firstCategoryDto.getIndustryCategoryNumber() );

        return firstCategory.build();
    }

    @Override
    public FirstCategoryDto toFirstCategoryDto(FirstCategory firstCategory) {
        if ( firstCategory == null ) {
            return null;
        }

        FirstCategoryDto firstCategoryDto = new FirstCategoryDto();

        firstCategoryDto.setNumber( firstCategory.getNumber() );
        firstCategoryDto.setKoreanName( firstCategory.getKoreanName() );
        firstCategoryDto.setEnglishName( firstCategory.getEnglishName() );
        firstCategoryDto.setIndustryCategoryNumber( firstCategory.getIndustryCategoryNumber() );

        return firstCategoryDto;
    }

    @Override
    public FirstCategory toFirstCategory(FirstCategoryDtoNoNumber firstCategoryDto) {
        if ( firstCategoryDto == null ) {
            return null;
        }

        FirstCategory.FirstCategoryBuilder firstCategory = FirstCategory.builder();

        firstCategory.koreanName( firstCategoryDto.getKoreanName() );
        firstCategory.englishName( firstCategoryDto.getEnglishName() );
        firstCategory.industryCategoryNumber( firstCategoryDto.getIndustryCategoryNumber() );

        return firstCategory.build();
    }

    @Override
    public FirstCategoryDtoNoNumber toFirstCategoryDtoNoNumber(FirstCategory firstCategory) {
        if ( firstCategory == null ) {
            return null;
        }

        FirstCategoryDtoNoNumber firstCategoryDtoNoNumber = new FirstCategoryDtoNoNumber();

        firstCategoryDtoNoNumber.setKoreanName( firstCategory.getKoreanName() );
        firstCategoryDtoNoNumber.setEnglishName( firstCategory.getEnglishName() );
        firstCategoryDtoNoNumber.setIndustryCategoryNumber( firstCategory.getIndustryCategoryNumber() );

        return firstCategoryDtoNoNumber;
    }
}

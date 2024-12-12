package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.aggregate.dto.SecondCategoryDto;
import site.hixview.aggregate.dto.SecondCategoryDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:02:20+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class SecondCategoryMapperImpl implements SecondCategoryMapper {

    @Override
    public SecondCategory toSecondCategory(SecondCategoryDto secondCategoryDto) {
        if ( secondCategoryDto == null ) {
            return null;
        }

        SecondCategory.SecondCategoryBuilder secondCategory = SecondCategory.builder();

        secondCategory.number( secondCategoryDto.getNumber() );
        secondCategory.koreanName( secondCategoryDto.getKoreanName() );
        secondCategory.englishName( secondCategoryDto.getEnglishName() );
        secondCategory.industryCategoryNumber( secondCategoryDto.getIndustryCategoryNumber() );
        secondCategory.firstCategoryNumber( secondCategoryDto.getFirstCategoryNumber() );

        return secondCategory.build();
    }

    @Override
    public SecondCategoryDto toSecondCategoryDto(SecondCategory secondCategory) {
        if ( secondCategory == null ) {
            return null;
        }

        SecondCategoryDto secondCategoryDto = new SecondCategoryDto();

        secondCategoryDto.setNumber( secondCategory.getNumber() );
        secondCategoryDto.setKoreanName( secondCategory.getKoreanName() );
        secondCategoryDto.setEnglishName( secondCategory.getEnglishName() );
        secondCategoryDto.setIndustryCategoryNumber( secondCategory.getIndustryCategoryNumber() );
        secondCategoryDto.setFirstCategoryNumber( secondCategory.getFirstCategoryNumber() );

        return secondCategoryDto;
    }

    @Override
    public SecondCategory toSecondCategory(SecondCategoryDtoNoNumber secondCategoryDto) {
        if ( secondCategoryDto == null ) {
            return null;
        }

        SecondCategory.SecondCategoryBuilder secondCategory = SecondCategory.builder();

        secondCategory.koreanName( secondCategoryDto.getKoreanName() );
        secondCategory.englishName( secondCategoryDto.getEnglishName() );
        secondCategory.industryCategoryNumber( secondCategoryDto.getIndustryCategoryNumber() );
        secondCategory.firstCategoryNumber( secondCategoryDto.getFirstCategoryNumber() );

        return secondCategory.build();
    }

    @Override
    public SecondCategoryDtoNoNumber toSecondCategoryDtoNoNumber(SecondCategory secondCategory) {
        if ( secondCategory == null ) {
            return null;
        }

        SecondCategoryDtoNoNumber secondCategoryDtoNoNumber = new SecondCategoryDtoNoNumber();

        secondCategoryDtoNoNumber.setKoreanName( secondCategory.getKoreanName() );
        secondCategoryDtoNoNumber.setEnglishName( secondCategory.getEnglishName() );
        secondCategoryDtoNoNumber.setIndustryCategoryNumber( secondCategory.getIndustryCategoryNumber() );
        secondCategoryDtoNoNumber.setFirstCategoryNumber( secondCategory.getFirstCategoryNumber() );

        return secondCategoryDtoNoNumber;
    }
}

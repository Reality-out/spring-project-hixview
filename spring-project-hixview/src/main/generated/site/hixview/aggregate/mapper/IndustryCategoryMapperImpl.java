package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.aggregate.dto.IndustryCategoryDto;
import site.hixview.aggregate.dto.IndustryCategoryDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryCategoryMapperImpl implements IndustryCategoryMapper {

    @Override
    public IndustryCategory toIndustryCategory(IndustryCategoryDto industryCategoryDto) {
        if ( industryCategoryDto == null ) {
            return null;
        }

        IndustryCategory.IndustryCategoryBuilder industryCategory = IndustryCategory.builder();

        industryCategory.number( industryCategoryDto.getNumber() );
        industryCategory.koreanName( industryCategoryDto.getKoreanName() );
        industryCategory.englishName( industryCategoryDto.getEnglishName() );

        return industryCategory.build();
    }

    @Override
    public IndustryCategoryDto toIndustryCategoryDto(IndustryCategory industryCategory) {
        if ( industryCategory == null ) {
            return null;
        }

        IndustryCategoryDto industryCategoryDto = new IndustryCategoryDto();

        industryCategoryDto.setNumber( industryCategory.getNumber() );
        industryCategoryDto.setKoreanName( industryCategory.getKoreanName() );
        industryCategoryDto.setEnglishName( industryCategory.getEnglishName() );

        return industryCategoryDto;
    }

    @Override
    public IndustryCategory toIndustryCategory(IndustryCategoryDtoNoNumber industryCategoryDto) {
        if ( industryCategoryDto == null ) {
            return null;
        }

        IndustryCategory.IndustryCategoryBuilder industryCategory = IndustryCategory.builder();

        industryCategory.koreanName( industryCategoryDto.getKoreanName() );
        industryCategory.englishName( industryCategoryDto.getEnglishName() );

        return industryCategory.build();
    }

    @Override
    public IndustryCategoryDtoNoNumber toIndustryCategoryDtoNoNumber(IndustryCategory industryCategory) {
        if ( industryCategory == null ) {
            return null;
        }

        IndustryCategoryDtoNoNumber industryCategoryDtoNoNumber = new IndustryCategoryDtoNoNumber();

        industryCategoryDtoNoNumber.setKoreanName( industryCategory.getKoreanName() );
        industryCategoryDtoNoNumber.setEnglishName( industryCategory.getEnglishName() );

        return industryCategoryDtoNoNumber;
    }
}

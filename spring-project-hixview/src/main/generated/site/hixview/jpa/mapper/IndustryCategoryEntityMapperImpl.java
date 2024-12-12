package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryCategory;
import site.hixview.jpa.entity.IndustryCategoryEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:27:06+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryCategoryEntityMapperImpl extends IndustryCategoryEntityMapper {

    @Override
    public IndustryCategoryEntity toIndustryCategoryEntity(IndustryCategory industryCategory) {
        if ( industryCategory == null ) {
            return null;
        }

        IndustryCategoryEntity industryCategoryEntity = new IndustryCategoryEntity();

        return industryCategoryEntity;
    }

    @Override
    public IndustryCategory toIndustryCategory(IndustryCategoryEntity industryCategoryEntity) {
        if ( industryCategoryEntity == null ) {
            return null;
        }

        IndustryCategory.IndustryCategoryBuilder industryCategory = IndustryCategory.builder();

        industryCategory.number( industryCategoryEntity.getNumber() );
        industryCategory.koreanName( industryCategoryEntity.getKoreanName() );
        industryCategory.englishName( industryCategoryEntity.getEnglishName() );

        return industryCategory.build();
    }
}

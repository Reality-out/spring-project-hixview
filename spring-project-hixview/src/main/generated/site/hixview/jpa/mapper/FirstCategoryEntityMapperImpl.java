package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.FirstCategory;
import site.hixview.jpa.entity.FirstCategoryEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:16:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class FirstCategoryEntityMapperImpl extends FirstCategoryEntityMapper {

    @Override
    public FirstCategoryEntity toFirstCategoryEntity(FirstCategory firstCategory) {
        if ( firstCategory == null ) {
            return null;
        }

        FirstCategoryEntity firstCategoryEntity = new FirstCategoryEntity();

        afterMappingToEntity( firstCategoryEntity, firstCategory );

        return firstCategoryEntity;
    }

    @Override
    public FirstCategory toFirstCategory(FirstCategoryEntity firstCategoryEntity) {
        if ( firstCategoryEntity == null ) {
            return null;
        }

        FirstCategory.FirstCategoryBuilder firstCategory = FirstCategory.builder();

        firstCategory.industryCategoryNumber( industryCategoryNumberToDomain( firstCategoryEntity.getIndustryCategory() ) );
        firstCategory.number( firstCategoryEntity.getNumber() );
        firstCategory.koreanName( firstCategoryEntity.getKoreanName() );
        firstCategory.englishName( firstCategoryEntity.getEnglishName() );

        return firstCategory.build();
    }
}

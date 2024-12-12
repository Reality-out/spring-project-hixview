package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.jpa.entity.SecondCategoryEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:33:17+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class SecondCategoryEntityMapperImpl extends SecondCategoryEntityMapper {

    @Override
    public SecondCategoryEntity toSecondCategoryEntity(SecondCategory secondCategory) {
        if ( secondCategory == null ) {
            return null;
        }

        SecondCategoryEntity secondCategoryEntity = new SecondCategoryEntity();

        afterMappingToEntity( secondCategoryEntity, secondCategory );

        return secondCategoryEntity;
    }

    @Override
    public SecondCategory toSecondCategory(SecondCategoryEntity secondCategoryEntity) {
        if ( secondCategoryEntity == null ) {
            return null;
        }

        SecondCategory.SecondCategoryBuilder secondCategory = SecondCategory.builder();

        secondCategory.industryCategoryNumber( industryCategoryNumberToDomain( secondCategoryEntity.getIndustryCategory() ) );
        secondCategory.firstCategoryNumber( firstCategoryNumberToDomain( secondCategoryEntity.getFirstCategory() ) );
        secondCategory.number( secondCategoryEntity.getNumber() );
        secondCategory.koreanName( secondCategoryEntity.getKoreanName() );
        secondCategory.englishName( secondCategoryEntity.getEnglishName() );

        return secondCategory.build();
    }
}

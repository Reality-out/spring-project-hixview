package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.SecondCategory;
import site.hixview.jpa.entity.SecondCategoryEntity;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.IndustryCategoryEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class SecondCategoryEntityMapperImpl implements SecondCategoryEntityMapper {

    @Override
    public SecondCategoryEntity toSecondCategoryEntity(SecondCategory secondCategory, IndustryCategoryEntityRepository industryCategoryRepository, FirstCategoryEntityRepository firstCategoryRepository) {
        if ( secondCategory == null ) {
            return null;
        }

        SecondCategoryEntity secondCategoryEntity = new SecondCategoryEntity();

        afterMappingToEntity( secondCategoryEntity, secondCategory, industryCategoryRepository, firstCategoryRepository );

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

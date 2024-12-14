package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;
import site.hixview.jpa.entity.CompanyEntity;
import site.hixview.jpa.repository.FirstCategoryEntityRepository;
import site.hixview.jpa.repository.SecondCategoryEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyEntityMapperImpl implements CompanyEntityMapper {

    @Override
    public CompanyEntity toCompanyEntity(Company company, FirstCategoryEntityRepository firstCategoryRepository, SecondCategoryEntityRepository secondCategoryRepository) {
        if ( company == null ) {
            return null;
        }

        CompanyEntity.CompanyEntityBuilder companyEntity = CompanyEntity.builder();

        companyEntity.code( company.getCode() );
        companyEntity.koreanName( company.getKoreanName() );
        companyEntity.englishName( company.getEnglishName() );
        companyEntity.nameListed( company.getNameListed() );
        if ( company.getCountryListed() != null ) {
            companyEntity.countryListed( company.getCountryListed().name() );
        }
        if ( company.getScale() != null ) {
            companyEntity.scale( company.getScale().name() );
        }

        return companyEntity.build();
    }

    @Override
    public Company toCompany(CompanyEntity companyEntity) {
        if ( companyEntity == null ) {
            return null;
        }

        Company.CompanyBuilder company = Company.builder();

        company.firstCategoryNumber( firstCategoryNumberToDomain( companyEntity.getFirstCategory() ) );
        company.secondCategoryNumber( secondCategoryNumberToDomain( companyEntity.getSecondCategory() ) );
        company.code( companyEntity.getCode() );
        company.koreanName( companyEntity.getKoreanName() );
        company.englishName( companyEntity.getEnglishName() );
        company.nameListed( companyEntity.getNameListed() );
        if ( companyEntity.getCountryListed() != null ) {
            company.countryListed( Enum.valueOf( Country.class, companyEntity.getCountryListed() ) );
        }
        if ( companyEntity.getScale() != null ) {
            company.scale( Enum.valueOf( Scale.class, companyEntity.getScale() ) );
        }

        return company.build();
    }
}

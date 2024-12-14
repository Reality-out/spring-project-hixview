package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Company;
import site.hixview.aggregate.dto.CompanyDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public Company toCompany(CompanyDto companyDto) {
        if ( companyDto == null ) {
            return null;
        }

        Company.CompanyBuilder company = Company.builder();

        company.countryListed( countryListedToDomain( companyDto.getCountryListed() ) );
        company.scale( scaleToDomain( companyDto.getScale() ) );
        company.code( companyDto.getCode() );
        company.koreanName( companyDto.getKoreanName() );
        company.englishName( companyDto.getEnglishName() );
        company.nameListed( companyDto.getNameListed() );
        company.firstCategoryNumber( companyDto.getFirstCategoryNumber() );
        company.secondCategoryNumber( companyDto.getSecondCategoryNumber() );

        return company.build();
    }

    @Override
    public CompanyDto toCompanyDto(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDto companyDto = new CompanyDto();

        companyDto.setCountryListed( countryListedToDto( company.getCountryListed() ) );
        companyDto.setScale( scaleToDto( company.getScale() ) );
        companyDto.setCode( company.getCode() );
        companyDto.setKoreanName( company.getKoreanName() );
        companyDto.setEnglishName( company.getEnglishName() );
        companyDto.setNameListed( company.getNameListed() );
        companyDto.setFirstCategoryNumber( company.getFirstCategoryNumber() );
        companyDto.setSecondCategoryNumber( company.getSecondCategoryNumber() );

        return companyDto;
    }
}

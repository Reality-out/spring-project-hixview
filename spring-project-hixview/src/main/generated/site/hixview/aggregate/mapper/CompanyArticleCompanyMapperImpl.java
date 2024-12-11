package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.aggregate.dto.CompanyArticleCompanyDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T23:32:01+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyArticleCompanyMapperImpl implements CompanyArticleCompanyMapper {

    @Override
    public CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyDto companyArticleCompanyDto) {
        if ( companyArticleCompanyDto == null ) {
            return null;
        }

        CompanyArticleCompany.CompanyArticleCompanyBuilder companyArticleCompany = CompanyArticleCompany.builder();

        companyArticleCompany.number( companyArticleCompanyDto.getNumber() );
        companyArticleCompany.articleNumber( companyArticleCompanyDto.getArticleNumber() );
        companyArticleCompany.companyCode( companyArticleCompanyDto.getCompanyCode() );

        return companyArticleCompany.build();
    }

    @Override
    public CompanyArticleCompanyDto toCompanyArticleCompanyDto(CompanyArticleCompany companyArticleCompany) {
        if ( companyArticleCompany == null ) {
            return null;
        }

        CompanyArticleCompanyDto companyArticleCompanyDto = new CompanyArticleCompanyDto();

        companyArticleCompanyDto.setNumber( companyArticleCompany.getNumber() );
        companyArticleCompanyDto.setArticleNumber( companyArticleCompany.getArticleNumber() );
        companyArticleCompanyDto.setCompanyCode( companyArticleCompany.getCompanyCode() );

        return companyArticleCompanyDto;
    }
}

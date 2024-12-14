package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.CompanyArticleCompany;
import site.hixview.jpa.entity.CompanyArticleCompanyEntity;
import site.hixview.jpa.repository.CompanyArticleEntityRepository;
import site.hixview.jpa.repository.CompanyEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyArticleCompanyEntityMapperImpl implements CompanyArticleCompanyEntityMapper {

    @Override
    public CompanyArticleCompanyEntity toCompanyArticleCompanyEntity(CompanyArticleCompany companyArticleCompany, CompanyArticleEntityRepository companyArticleRepository, CompanyEntityRepository companyEntityRepository) {
        if ( companyArticleCompany == null ) {
            return null;
        }

        CompanyArticleCompanyEntity companyArticleCompanyEntity = new CompanyArticleCompanyEntity();

        afterMappingToEntity( companyArticleCompanyEntity, companyArticleCompany, companyArticleRepository, companyEntityRepository );

        return companyArticleCompanyEntity;
    }

    @Override
    public CompanyArticleCompany toCompanyArticleCompany(CompanyArticleCompanyEntity companyArticleCompanyEntity) {
        if ( companyArticleCompanyEntity == null ) {
            return null;
        }

        CompanyArticleCompany.CompanyArticleCompanyBuilder companyArticleCompany = CompanyArticleCompany.builder();

        companyArticleCompany.articleNumber( articleNumberToDomain( companyArticleCompanyEntity.getCompanyArticle() ) );
        companyArticleCompany.companyCode( companyCodeToDomain( companyArticleCompanyEntity.getCompany() ) );
        companyArticleCompany.number( companyArticleCompanyEntity.getNumber() );

        return companyArticleCompany.build();
    }
}

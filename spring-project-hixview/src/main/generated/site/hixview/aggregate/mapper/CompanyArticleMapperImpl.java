package site.hixview.aggregate.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.dto.CompanyArticleDto;
import site.hixview.aggregate.dto.CompanyArticleDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:23:41+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyArticleMapperImpl implements CompanyArticleMapper {

    @Override
    public CompanyArticle toCompanyArticle(CompanyArticleDto companyArticleDto) {
        if ( companyArticleDto == null ) {
            return null;
        }

        CompanyArticle.CompanyArticleBuilder companyArticle = CompanyArticle.builder();

        companyArticle.subjectCountry( subjectCountryToDomain( companyArticleDto.getSubjectCountry() ) );
        companyArticle.importance( importanceToDomain( companyArticleDto.getImportance() ) );
        companyArticle.mappedCompanyCodes( mappedCompanyCodesToDomain( companyArticleDto.getMappedCompanyCodes() ) );
        companyArticle.number( companyArticleDto.getNumber() );
        companyArticle.name( companyArticleDto.getName() );
        companyArticle.link( companyArticleDto.getLink() );
        if ( companyArticleDto.getDate() != null ) {
            companyArticle.date( LocalDate.parse( companyArticleDto.getDate() ) );
        }
        companyArticle.summary( companyArticleDto.getSummary() );
        companyArticle.pressNumber( companyArticleDto.getPressNumber() );

        return companyArticle.build();
    }

    @Override
    public CompanyArticleDto toCompanyArticleDto(CompanyArticle companyArticle) {
        if ( companyArticle == null ) {
            return null;
        }

        CompanyArticleDto companyArticleDto = new CompanyArticleDto();

        companyArticleDto.setSubjectCountry( subjectCountryToDto( companyArticle.getSubjectCountry() ) );
        companyArticleDto.setImportance( importanceToDto( companyArticle.getImportance() ) );
        companyArticleDto.setMappedCompanyCodes( mappedCompanyCodesToDto( companyArticle.getMappedCompanyCodes() ) );
        companyArticleDto.setNumber( companyArticle.getNumber() );
        companyArticleDto.setName( companyArticle.getName() );
        companyArticleDto.setLink( companyArticle.getLink() );
        if ( companyArticle.getDate() != null ) {
            companyArticleDto.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( companyArticle.getDate() ) );
        }
        companyArticleDto.setSummary( companyArticle.getSummary() );
        companyArticleDto.setPressNumber( companyArticle.getPressNumber() );

        return companyArticleDto;
    }

    @Override
    public CompanyArticle toCompanyArticle(CompanyArticleDtoNoNumber companyArticleDto) {
        if ( companyArticleDto == null ) {
            return null;
        }

        CompanyArticle.CompanyArticleBuilder companyArticle = CompanyArticle.builder();

        companyArticle.subjectCountry( subjectCountryToDomain( companyArticleDto.getSubjectCountry() ) );
        companyArticle.importance( importanceToDomain( companyArticleDto.getImportance() ) );
        companyArticle.mappedCompanyCodes( mappedCompanyCodesToDomain( companyArticleDto.getMappedCompanyCodes() ) );
        companyArticle.name( companyArticleDto.getName() );
        companyArticle.link( companyArticleDto.getLink() );
        if ( companyArticleDto.getDate() != null ) {
            companyArticle.date( LocalDate.parse( companyArticleDto.getDate() ) );
        }
        companyArticle.summary( companyArticleDto.getSummary() );
        companyArticle.pressNumber( companyArticleDto.getPressNumber() );

        return companyArticle.build();
    }

    @Override
    public CompanyArticleDtoNoNumber toCompanyArticleDtoNoNumber(CompanyArticle companyArticle) {
        if ( companyArticle == null ) {
            return null;
        }

        CompanyArticleDtoNoNumber companyArticleDtoNoNumber = new CompanyArticleDtoNoNumber();

        companyArticleDtoNoNumber.setSubjectCountry( subjectCountryToDto( companyArticle.getSubjectCountry() ) );
        companyArticleDtoNoNumber.setImportance( importanceToDto( companyArticle.getImportance() ) );
        companyArticleDtoNoNumber.setMappedCompanyCodes( mappedCompanyCodesToDto( companyArticle.getMappedCompanyCodes() ) );
        companyArticleDtoNoNumber.setName( companyArticle.getName() );
        companyArticleDtoNoNumber.setLink( companyArticle.getLink() );
        if ( companyArticle.getDate() != null ) {
            companyArticleDtoNoNumber.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( companyArticle.getDate() ) );
        }
        companyArticleDtoNoNumber.setSummary( companyArticle.getSummary() );
        companyArticleDtoNoNumber.setPressNumber( companyArticle.getPressNumber() );

        return companyArticleDtoNoNumber;
    }
}

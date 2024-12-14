package site.hixview.aggregate.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.dto.IndustryArticleDto;
import site.hixview.aggregate.dto.IndustryArticleDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:23:41+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class IndustryArticleMapperImpl implements IndustryArticleMapper {

    @Override
    public IndustryArticle toIndustryArticle(IndustryArticleDto industryArticleDto) {
        if ( industryArticleDto == null ) {
            return null;
        }

        IndustryArticle.IndustryArticleBuilder industryArticle = IndustryArticle.builder();

        industryArticle.subjectCountry( subjectCountryToDomain( industryArticleDto.getSubjectCountry() ) );
        industryArticle.importance( importanceToDomain( industryArticleDto.getImportance() ) );
        industryArticle.mappedSecondCategoryNumbers( mappedSecondCategoryNumbersToDomain( industryArticleDto.getMappedSecondCategoryNumbers() ) );
        industryArticle.number( industryArticleDto.getNumber() );
        industryArticle.name( industryArticleDto.getName() );
        industryArticle.link( industryArticleDto.getLink() );
        if ( industryArticleDto.getDate() != null ) {
            industryArticle.date( LocalDate.parse( industryArticleDto.getDate() ) );
        }
        industryArticle.summary( industryArticleDto.getSummary() );
        industryArticle.pressNumber( industryArticleDto.getPressNumber() );
        industryArticle.firstCategoryNumber( industryArticleDto.getFirstCategoryNumber() );

        return industryArticle.build();
    }

    @Override
    public IndustryArticleDto toIndustryArticleDto(IndustryArticle industryArticle) {
        if ( industryArticle == null ) {
            return null;
        }

        IndustryArticleDto industryArticleDto = new IndustryArticleDto();

        industryArticleDto.setSubjectCountry( subjectCountryToDto( industryArticle.getSubjectCountry() ) );
        industryArticleDto.setImportance( importanceToDto( industryArticle.getImportance() ) );
        industryArticleDto.setMappedSecondCategoryNumbers( mappedSecondCategoryNumbersToDto( industryArticle.getMappedSecondCategoryNumbers() ) );
        industryArticleDto.setNumber( industryArticle.getNumber() );
        industryArticleDto.setName( industryArticle.getName() );
        industryArticleDto.setLink( industryArticle.getLink() );
        if ( industryArticle.getDate() != null ) {
            industryArticleDto.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( industryArticle.getDate() ) );
        }
        industryArticleDto.setSummary( industryArticle.getSummary() );
        industryArticleDto.setPressNumber( industryArticle.getPressNumber() );
        industryArticleDto.setFirstCategoryNumber( industryArticle.getFirstCategoryNumber() );

        return industryArticleDto;
    }

    @Override
    public IndustryArticle toIndustryArticle(IndustryArticleDtoNoNumber industryArticleDto) {
        if ( industryArticleDto == null ) {
            return null;
        }

        IndustryArticle.IndustryArticleBuilder industryArticle = IndustryArticle.builder();

        industryArticle.subjectCountry( subjectCountryToDomain( industryArticleDto.getSubjectCountry() ) );
        industryArticle.importance( importanceToDomain( industryArticleDto.getImportance() ) );
        industryArticle.mappedSecondCategoryNumbers( mappedSecondCategoryNumbersToDomain( industryArticleDto.getMappedSecondCategoryNumbers() ) );
        industryArticle.name( industryArticleDto.getName() );
        industryArticle.link( industryArticleDto.getLink() );
        if ( industryArticleDto.getDate() != null ) {
            industryArticle.date( LocalDate.parse( industryArticleDto.getDate() ) );
        }
        industryArticle.summary( industryArticleDto.getSummary() );
        industryArticle.pressNumber( industryArticleDto.getPressNumber() );
        industryArticle.firstCategoryNumber( industryArticleDto.getFirstCategoryNumber() );

        return industryArticle.build();
    }

    @Override
    public IndustryArticleDtoNoNumber toIndustryArticleDtoNoNumber(IndustryArticle industryArticle) {
        if ( industryArticle == null ) {
            return null;
        }

        IndustryArticleDtoNoNumber industryArticleDtoNoNumber = new IndustryArticleDtoNoNumber();

        industryArticleDtoNoNumber.setSubjectCountry( subjectCountryToDto( industryArticle.getSubjectCountry() ) );
        industryArticleDtoNoNumber.setImportance( importanceToDto( industryArticle.getImportance() ) );
        industryArticleDtoNoNumber.setMappedSecondCategoryNumbers( mappedSecondCategoryNumbersToDto( industryArticle.getMappedSecondCategoryNumbers() ) );
        industryArticleDtoNoNumber.setName( industryArticle.getName() );
        industryArticleDtoNoNumber.setLink( industryArticle.getLink() );
        if ( industryArticle.getDate() != null ) {
            industryArticleDtoNoNumber.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( industryArticle.getDate() ) );
        }
        industryArticleDtoNoNumber.setSummary( industryArticle.getSummary() );
        industryArticleDtoNoNumber.setPressNumber( industryArticle.getPressNumber() );
        industryArticleDtoNoNumber.setFirstCategoryNumber( industryArticle.getFirstCategoryNumber() );

        return industryArticleDtoNoNumber;
    }
}

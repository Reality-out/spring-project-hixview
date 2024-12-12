package site.hixview.aggregate.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.dto.EconomyArticleDto;
import site.hixview.aggregate.dto.EconomyArticleDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:07:36+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class EconomyArticleMapperImpl implements EconomyArticleMapper {

    @Override
    public EconomyArticle toEconomyArticle(EconomyArticleDto economyArticleDto) {
        if ( economyArticleDto == null ) {
            return null;
        }

        EconomyArticle.EconomyArticleBuilder economyArticle = EconomyArticle.builder();

        economyArticle.subjectCountry( subjectCountryToDomain( economyArticleDto.getSubjectCountry() ) );
        economyArticle.importance( importanceToDomain( economyArticleDto.getImportance() ) );
        economyArticle.mappedEconomyContentNumbers( mappedEconomyContentNumbersToDomain( economyArticleDto.getMappedEconomyContentNumbers() ) );
        economyArticle.number( economyArticleDto.getNumber() );
        economyArticle.name( economyArticleDto.getName() );
        economyArticle.link( economyArticleDto.getLink() );
        if ( economyArticleDto.getDate() != null ) {
            economyArticle.date( LocalDate.parse( economyArticleDto.getDate() ) );
        }
        economyArticle.summary( economyArticleDto.getSummary() );
        economyArticle.pressNumber( economyArticleDto.getPressNumber() );

        return economyArticle.build();
    }

    @Override
    public EconomyArticleDto toEconomyArticleDto(EconomyArticle economyArticle) {
        if ( economyArticle == null ) {
            return null;
        }

        EconomyArticleDto economyArticleDto = new EconomyArticleDto();

        economyArticleDto.setSubjectCountry( subjectCountryToDto( economyArticle.getSubjectCountry() ) );
        economyArticleDto.setImportance( importanceToDto( economyArticle.getImportance() ) );
        economyArticleDto.setMappedEconomyContentNumbers( mappedEconomyContentNumbersToDto( economyArticle.getMappedEconomyContentNumbers() ) );
        economyArticleDto.setNumber( economyArticle.getNumber() );
        economyArticleDto.setName( economyArticle.getName() );
        economyArticleDto.setLink( economyArticle.getLink() );
        if ( economyArticle.getDate() != null ) {
            economyArticleDto.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( economyArticle.getDate() ) );
        }
        economyArticleDto.setSummary( economyArticle.getSummary() );
        economyArticleDto.setPressNumber( economyArticle.getPressNumber() );

        return economyArticleDto;
    }

    @Override
    public EconomyArticle toEconomyArticle(EconomyArticleDtoNoNumber economyArticleDto) {
        if ( economyArticleDto == null ) {
            return null;
        }

        EconomyArticle.EconomyArticleBuilder economyArticle = EconomyArticle.builder();

        economyArticle.subjectCountry( subjectCountryToDomain( economyArticleDto.getSubjectCountry() ) );
        economyArticle.importance( importanceToDomain( economyArticleDto.getImportance() ) );
        economyArticle.mappedEconomyContentNumbers( mappedEconomyContentNumbersToDomain( economyArticleDto.getMappedEconomyContentNumbers() ) );
        economyArticle.name( economyArticleDto.getName() );
        economyArticle.link( economyArticleDto.getLink() );
        if ( economyArticleDto.getDate() != null ) {
            economyArticle.date( LocalDate.parse( economyArticleDto.getDate() ) );
        }
        economyArticle.summary( economyArticleDto.getSummary() );
        economyArticle.pressNumber( economyArticleDto.getPressNumber() );

        return economyArticle.build();
    }

    @Override
    public EconomyArticleDtoNoNumber toEconomyArticleDtoNoNumber(EconomyArticle economyArticle) {
        if ( economyArticle == null ) {
            return null;
        }

        EconomyArticleDtoNoNumber economyArticleDtoNoNumber = new EconomyArticleDtoNoNumber();

        economyArticleDtoNoNumber.setSubjectCountry( subjectCountryToDto( economyArticle.getSubjectCountry() ) );
        economyArticleDtoNoNumber.setImportance( importanceToDto( economyArticle.getImportance() ) );
        economyArticleDtoNoNumber.setMappedEconomyContentNumbers( mappedEconomyContentNumbersToDto( economyArticle.getMappedEconomyContentNumbers() ) );
        economyArticleDtoNoNumber.setName( economyArticle.getName() );
        economyArticleDtoNoNumber.setLink( economyArticle.getLink() );
        if ( economyArticle.getDate() != null ) {
            economyArticleDtoNoNumber.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( economyArticle.getDate() ) );
        }
        economyArticleDtoNoNumber.setSummary( economyArticle.getSummary() );
        economyArticleDtoNoNumber.setPressNumber( economyArticle.getPressNumber() );

        return economyArticleDtoNoNumber;
    }
}

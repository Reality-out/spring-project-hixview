package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.CompanyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.CompanyArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.CompanyArticleCompanyEntityRepository;
import site.hixview.jpa.repository.PressEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class CompanyArticleEntityMapperImpl implements CompanyArticleEntityMapper {

    @Override
    public CompanyArticleEntity toCompanyArticleEntity(CompanyArticle companyArticle, ArticleEntityRepository articleEntityRepository, PressEntityRepository pressEntityRepository) {
        if ( companyArticle == null ) {
            return null;
        }

        CompanyArticleEntity.CompanyArticleEntityBuilder companyArticleEntity = CompanyArticleEntity.builder();

        companyArticleEntity.name( companyArticle.getName() );
        companyArticleEntity.link( companyArticle.getLink() );
        companyArticleEntity.date( companyArticle.getDate() );
        if ( companyArticle.getSubjectCountry() != null ) {
            companyArticleEntity.subjectCountry( companyArticle.getSubjectCountry().name() );
        }
        if ( companyArticle.getImportance() != null ) {
            companyArticleEntity.importance( companyArticle.getImportance().name() );
        }
        companyArticleEntity.summary( companyArticle.getSummary() );

        return companyArticleEntity.build();
    }

    @Override
    public CompanyArticle toCompanyArticle(CompanyArticleEntity companyArticleEntity, CompanyArticleCompanyEntityRepository companyArticleCompanyRepository) {
        if ( companyArticleEntity == null ) {
            return null;
        }

        CompanyArticle.CompanyArticleBuilder companyArticle = CompanyArticle.builder();

        companyArticle.number( numberToDomain( companyArticleEntity.getArticle() ) );
        companyArticle.pressNumber( pressNumberToDomain( companyArticleEntity.getPress() ) );
        companyArticle.name( companyArticleEntity.getName() );
        companyArticle.link( companyArticleEntity.getLink() );
        companyArticle.date( companyArticleEntity.getDate() );
        if ( companyArticleEntity.getSubjectCountry() != null ) {
            companyArticle.subjectCountry( Enum.valueOf( Country.class, companyArticleEntity.getSubjectCountry() ) );
        }
        if ( companyArticleEntity.getImportance() != null ) {
            companyArticle.importance( Enum.valueOf( Importance.class, companyArticleEntity.getImportance() ) );
        }
        companyArticle.summary( companyArticleEntity.getSummary() );

        return companyArticle.build();
    }
}

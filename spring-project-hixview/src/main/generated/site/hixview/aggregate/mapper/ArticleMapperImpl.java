package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Article;
import site.hixview.aggregate.dto.ArticleDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public Article toArticle(ArticleDto articleDto) {
        if ( articleDto == null ) {
            return null;
        }

        Article.ArticleBuilder article = Article.builder();

        article.number( articleDto.getNumber() );

        return article.build();
    }

    @Override
    public ArticleDto toArticleDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleDto articleDto = new ArticleDto();

        articleDto.setNumber( article.getNumber() );

        return articleDto;
    }
}

package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Article;
import site.hixview.jpa.entity.ArticleEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:16:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ArticleEntityMapperImpl extends ArticleEntityMapper {

    @Override
    public ArticleEntity toArticleEntity(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleEntity articleEntity = new ArticleEntity();

        return articleEntity;
    }

    @Override
    public Article toArticle(ArticleEntity articleEntity) {
        if ( articleEntity == null ) {
            return null;
        }

        Article.ArticleBuilder article = Article.builder();

        article.number( articleEntity.getNumber() );

        return article.build();
    }
}

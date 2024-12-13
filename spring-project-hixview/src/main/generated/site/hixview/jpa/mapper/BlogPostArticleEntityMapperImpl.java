package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.jpa.entity.BlogPostArticleEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T21:32:55+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BlogPostArticleEntityMapperImpl extends BlogPostArticleEntityMapper {

    @Override
    public BlogPostArticleEntity toBlogPostArticleEntity(BlogPostArticle blogPostArticle) {
        if ( blogPostArticle == null ) {
            return null;
        }

        BlogPostArticleEntity blogPostArticleEntity = new BlogPostArticleEntity();

        afterMappingToEntity( blogPostArticleEntity, blogPostArticle );

        return blogPostArticleEntity;
    }

    @Override
    public BlogPostArticle toBlogPostArticle(BlogPostArticleEntity blogPostArticleEntity) {
        if ( blogPostArticleEntity == null ) {
            return null;
        }

        BlogPostArticle.BlogPostArticleBuilder blogPostArticle = BlogPostArticle.builder();

        blogPostArticle.postNumber( postNumberToDomain( blogPostArticleEntity.getBlogPost() ) );
        blogPostArticle.articleNumber( articleNumberToDomain( blogPostArticleEntity.getArticle() ) );
        blogPostArticle.number( blogPostArticleEntity.getNumber() );

        return blogPostArticle.build();
    }
}

package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.jpa.entity.BlogPostArticleEntity;
import site.hixview.jpa.repository.ArticleEntityRepository;
import site.hixview.jpa.repository.BlogPostEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:09:29+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BlogPostArticleEntityMapperImpl implements BlogPostArticleEntityMapper {

    @Override
    public BlogPostArticleEntity toBlogPostArticleEntity(BlogPostArticle blogPostArticle, BlogPostEntityRepository blogPostEntityRepository, ArticleEntityRepository articleEntityRepository) {
        if ( blogPostArticle == null ) {
            return null;
        }

        BlogPostArticleEntity blogPostArticleEntity = new BlogPostArticleEntity();

        afterMappingToEntity( blogPostArticleEntity, blogPostArticle, blogPostEntityRepository, articleEntityRepository );

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

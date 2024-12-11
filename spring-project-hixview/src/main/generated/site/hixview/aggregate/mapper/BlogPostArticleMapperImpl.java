package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.BlogPostArticle;
import site.hixview.aggregate.dto.BlogPostArticleDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-11T21:42:34+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BlogPostArticleMapperImpl implements BlogPostArticleMapper {

    @Override
    public BlogPostArticle toBlogPostArticle(BlogPostArticleDto blogPostDto) {
        if ( blogPostDto == null ) {
            return null;
        }

        BlogPostArticle.BlogPostArticleBuilder blogPostArticle = BlogPostArticle.builder();

        blogPostArticle.number( blogPostDto.getNumber() );
        blogPostArticle.postNumber( blogPostDto.getPostNumber() );
        blogPostArticle.articleNumber( blogPostDto.getArticleNumber() );

        return blogPostArticle.build();
    }

    @Override
    public BlogPostArticleDto toBlogPostArticleDto(BlogPostArticle blogPost) {
        if ( blogPost == null ) {
            return null;
        }

        BlogPostArticleDto blogPostArticleDto = new BlogPostArticleDto();

        blogPostArticleDto.setNumber( blogPost.getNumber() );
        blogPostArticleDto.setPostNumber( blogPost.getPostNumber() );
        blogPostArticleDto.setArticleNumber( blogPost.getArticleNumber() );

        return blogPostArticleDto;
    }
}

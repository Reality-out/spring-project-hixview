package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.enums.Classification;
import site.hixview.jpa.entity.BlogPostEntity;
import site.hixview.jpa.repository.BlogPostArticleEntityRepository;
import site.hixview.jpa.repository.PostEntityRepository;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T12:52:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BlogPostEntityMapperImpl implements BlogPostEntityMapper {

    @Override
    public BlogPostEntity toBlogPostEntity(BlogPost blogPost, PostEntityRepository postEntityRepository) {
        if ( blogPost == null ) {
            return null;
        }

        BlogPostEntity.BlogPostEntityBuilder blogPostEntity = BlogPostEntity.builder();

        blogPostEntity.name( blogPost.getName() );
        blogPostEntity.link( blogPost.getLink() );
        blogPostEntity.date( blogPost.getDate() );
        if ( blogPost.getClassification() != null ) {
            blogPostEntity.classification( blogPost.getClassification().name() );
        }

        return blogPostEntity.build();
    }

    @Override
    public BlogPost toBlogPost(BlogPostEntity blogPostEntity, BlogPostArticleEntityRepository blogPostArticleRepository) {
        if ( blogPostEntity == null ) {
            return null;
        }

        BlogPost.BlogPostBuilder blogPost = BlogPost.builder();

        blogPost.number( blogPostEntity.getNumber() );
        blogPost.name( blogPostEntity.getName() );
        blogPost.link( blogPostEntity.getLink() );
        blogPost.date( blogPostEntity.getDate() );
        if ( blogPostEntity.getClassification() != null ) {
            blogPost.classification( Enum.valueOf( Classification.class, blogPostEntity.getClassification() ) );
        }

        return blogPost.build();
    }
}

package site.hixview.aggregate.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.BlogPost;
import site.hixview.aggregate.dto.BlogPostDto;
import site.hixview.aggregate.dto.BlogPostDtoNoNumber;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T18:23:40+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BlogPostMapperImpl implements BlogPostMapper {

    @Override
    public BlogPost toBlogPost(BlogPostDto blogPostDto) {
        if ( blogPostDto == null ) {
            return null;
        }

        BlogPost.BlogPostBuilder blogPost = BlogPost.builder();

        blogPost.classification( classificationToDomain( blogPostDto.getClassification() ) );
        blogPost.mappedArticleNumbers( mappedArticleNumbersToDomain( blogPostDto.getMappedArticleNumbers() ) );
        blogPost.number( blogPostDto.getNumber() );
        blogPost.name( blogPostDto.getName() );
        blogPost.link( blogPostDto.getLink() );
        if ( blogPostDto.getDate() != null ) {
            blogPost.date( LocalDate.parse( blogPostDto.getDate() ) );
        }

        return blogPost.build();
    }

    @Override
    public BlogPostDto toBlogPostDto(BlogPost blogPost) {
        if ( blogPost == null ) {
            return null;
        }

        BlogPostDto blogPostDto = new BlogPostDto();

        blogPostDto.setClassification( classificationToDto( blogPost.getClassification() ) );
        blogPostDto.setMappedArticleNumbers( mappedArticleNumbersToDto( blogPost.getMappedArticleNumbers() ) );
        blogPostDto.setNumber( blogPost.getNumber() );
        blogPostDto.setName( blogPost.getName() );
        blogPostDto.setLink( blogPost.getLink() );
        if ( blogPost.getDate() != null ) {
            blogPostDto.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( blogPost.getDate() ) );
        }

        return blogPostDto;
    }

    @Override
    public BlogPost toBlogPost(BlogPostDtoNoNumber blogPostDto) {
        if ( blogPostDto == null ) {
            return null;
        }

        BlogPost.BlogPostBuilder blogPost = BlogPost.builder();

        blogPost.classification( classificationToDomain( blogPostDto.getClassification() ) );
        blogPost.mappedArticleNumbers( mappedArticleNumbersToDomain( blogPostDto.getMappedArticleNumbers() ) );
        blogPost.name( blogPostDto.getName() );
        blogPost.link( blogPostDto.getLink() );
        if ( blogPostDto.getDate() != null ) {
            blogPost.date( LocalDate.parse( blogPostDto.getDate() ) );
        }

        return blogPost.build();
    }

    @Override
    public BlogPostDtoNoNumber toBlogPostDtoNoNumber(BlogPost blogPost) {
        if ( blogPost == null ) {
            return null;
        }

        BlogPostDtoNoNumber blogPostDtoNoNumber = new BlogPostDtoNoNumber();

        blogPostDtoNoNumber.setClassification( classificationToDto( blogPost.getClassification() ) );
        blogPostDtoNoNumber.setMappedArticleNumbers( mappedArticleNumbersToDto( blogPost.getMappedArticleNumbers() ) );
        blogPostDtoNoNumber.setName( blogPost.getName() );
        blogPostDtoNoNumber.setLink( blogPost.getLink() );
        if ( blogPost.getDate() != null ) {
            blogPostDtoNoNumber.setDate( DateTimeFormatter.ISO_LOCAL_DATE.format( blogPost.getDate() ) );
        }

        return blogPostDtoNoNumber;
    }
}

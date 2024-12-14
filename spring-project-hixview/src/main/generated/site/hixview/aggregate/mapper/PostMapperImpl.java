package site.hixview.aggregate.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.dto.PostDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-14T11:41:28+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post toPost(PostDto postDto) {
        if ( postDto == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.number( postDto.getNumber() );

        return post.build();
    }

    @Override
    public PostDto toPostDto(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDto postDto = new PostDto();

        postDto.setNumber( post.getNumber() );

        return postDto;
    }
}

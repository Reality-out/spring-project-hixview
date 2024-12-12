package site.hixview.jpa.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import site.hixview.aggregate.domain.Post;
import site.hixview.jpa.entity.PostEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-13T00:27:06+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class PostEntityMapperImpl extends PostEntityMapper {

    @Override
    public PostEntity toPostEntity(Post post) {
        if ( post == null ) {
            return null;
        }

        PostEntity postEntity = new PostEntity();

        return postEntity;
    }

    @Override
    public Post toPost(PostEntity postEntity) {
        if ( postEntity == null ) {
            return null;
        }

        Post.PostBuilder post = Post.builder();

        post.number( postEntity.getNumber() );

        return post.build();
    }
}

package site.hixview.aggregate.mapper;

import org.mapstruct.Mapper;
import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.dto.PostDto;

@Mapper
public interface PostMapper {
    Post toPost(PostDto postDto);

    PostDto toPostDto(Post post);
}

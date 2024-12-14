package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.PostDto;
import site.hixview.support.spring.util.PostTestUtils;

public interface PostDtoTestUtils extends PostTestUtils {
    /**
     * Create
     */
    default PostDto createPostDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(post.getNumber());
        return postDto;
    }

    default PostDto createAnotherPostDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(anotherPost.getNumber());
        return postDto;
    }
}

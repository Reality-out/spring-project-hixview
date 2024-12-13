package site.hixview.support.spring.util.dto;

import site.hixview.aggregate.dto.PostDto;

public interface PostDtoTestUtils {
    /**
     * Create
     */
    default PostDto createPostDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(1L);
        return postDto;
    }

    default PostDto createAnotherPostDto() {
        PostDto postDto = new PostDto();
        postDto.setNumber(2L);
        return postDto;
    }
}

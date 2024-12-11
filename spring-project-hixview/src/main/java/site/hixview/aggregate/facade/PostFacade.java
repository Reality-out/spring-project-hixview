package site.hixview.aggregate.facade;

import site.hixview.aggregate.domain.Post;
import site.hixview.aggregate.domain.Post.PostBuilder;
import site.hixview.aggregate.dto.PostDto;

public class PostFacade {
    public PostBuilder createBuilder(final Post post) {
        return Post.builder()
                .number(post.getNumber());
    }

    public PostBuilder createBuilder(final PostDto post) {
        return Post.builder()
                .number(post.getNumber());
    }
}
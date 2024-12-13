package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.Post;

public interface PostTestUtils {
    Post post = Post.builder().number(1L).build();
    Post anotherPost = Post.builder().number(2L).build();
}

package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.PostEntity;
import site.hixview.support.spring.util.PostTestUtils;

public interface PostEntityTestUtils extends PostTestUtils {
    /**
     * Create
     */
    default PostEntity createPostEntity() {
        return new PostEntity();
    }

    default PostEntity createAnotherPostEntity() {
        return new PostEntity();
    }

    default PostEntity createNumberedPostEntity() {
        PostEntity postEntity = new PostEntity();
        postEntity.updateNumber(post.getNumber());
        return postEntity;
    }
}

package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.PostEntity;

public interface PostEntityTestUtils {
    /**
     * Create
     */
    default PostEntity createPostEntity() {
        return new PostEntity();
    }

    default PostEntity createAnotherPostEntity() {
        return new PostEntity();
    }
}

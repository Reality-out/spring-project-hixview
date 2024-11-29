package site.hixview.support.jpa.util;

import site.hixview.jpa.entity.PostEntity;

public interface PostTestUtils {
    /**
     * Create
     */
    default PostEntity createPost() {
        return new PostEntity();
    }

    default PostEntity createAnotherPost() {
        return new PostEntity();
    }
}

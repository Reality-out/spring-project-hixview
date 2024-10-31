package site.hixview.domain.repository;


import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.BlogPost;

import java.util.List;
import java.util.Optional;

public interface BlogPostRepository {
    /**
     * SELECT BlogPost
     */
    List<BlogPost> getPosts();

    List<BlogPost> getLatestPosts(Classification classification);

    Optional<BlogPost> getPostByNumber(Long number);

    Optional<BlogPost> getPostByName(String name);

    Optional<BlogPost> getPostByLink(String link);

    /**
     * INSERT BlogPost
     */
    Long savePost(BlogPost article);

    /**
     * UPDATE BlogPost
     */
    void updatePost(BlogPost article);

    /**
     * REMOVE BlogPost
     */
    void deletePostByName(String name);
}

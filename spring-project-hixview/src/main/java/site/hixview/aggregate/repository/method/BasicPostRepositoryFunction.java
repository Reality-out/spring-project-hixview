package site.hixview.aggregate.repository.method;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BasicPostRepositoryFunction<T> {
    /**
     * SELECT Post
     */
    List<T> getPosts();

    List<T> getPostsByDate(LocalDate date);

    List<T> getPostsByDate(LocalDate startDate, LocalDate endDate);

    List<T> getLatestPosts();

    Optional<T> getPostByNumber(Long number);

    Optional<T> getPostByName(String name);

    Optional<T> getPostByLink(String link);

    /**
     * INSERT Post
     */
    Long savePost(T post);

    /**
     * UPDATE Post
     */
    void updatePost(T post);

    /**
     * REMOVE Post
     */
    void deletePostByNumber(Long number);
}

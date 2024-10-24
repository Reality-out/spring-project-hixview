package site.hixview.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.error.AlreadyExistException;
import site.hixview.domain.error.NotFoundException;
import site.hixview.domain.repository.BlogPostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static site.hixview.domain.vo.ExceptionMessage.*;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogPostService {

    private final BlogPostRepository postRepository;

    /**
     * SELECT BlogPost
     */
    public List<BlogPost> findPosts() {
        return postRepository.getPosts();
    }

    public Optional<BlogPost> findPostByNumber(Long number) {
        return postRepository.getPostByNumber(number);
    }

    public Optional<BlogPost> findPostByName(String name) {
        return postRepository.getPostByName(name);
    }

    public Optional<BlogPost> findPostByLink(String link) {
        return postRepository.getPostByLink(link);
    }

    public Optional<BlogPost> findPostByNumberOrName(String numberOrName) {
        return NUMBER_PATTERN.matcher(numberOrName).matches() ?
                findPostByNumber(Long.parseLong(numberOrName)) : findPostByName(numberOrName);
    }

    /**
     * INSERT BlogPost
     */
    @Transactional
    public List<BlogPost> registerPosts(BlogPost... posts) {
        List<BlogPost> postList = new ArrayList<>();
        for (BlogPost post : posts) {
            postList.add(BlogPost.builder().blogPost(post).number(registerPost(post).getNumber()).build());
        }
        return postList;
    }

    @Transactional
    public BlogPost registerPost(BlogPost post) {
        duplicateCheck(post);
        return BlogPost.builder().blogPost(post).number(postRepository.savePost(post)).build();
    }

    /**
     * UPDATE BlogPost
     */
    @Transactional
    public void correctPost(BlogPost post) {
        existentCheck(post.getName());
        postRepository.updatePost(post);
    }

    /**
     * REMOVE BlogPost
     */
    @Transactional
    public void removePostByName(String name) {
        existentCheck(name);
        postRepository.deletePostByName(name);
    }

    /**
     * Other private methods
     */
    private void duplicateCheck(BlogPost post) {
        postRepository.getPostByName(post.getName()).ifPresent(
                v -> {throw new AlreadyExistException(ALREADY_EXIST_BLOG_POST_NAME);}
        );
    }

    private void existentCheck(String name) {
        postRepository.getPostByName(name).orElseThrow(
                () -> new NotFoundException(NO_BLOG_POST_WITH_THAT_NAME)
        );
    }
}

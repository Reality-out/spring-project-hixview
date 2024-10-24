package site.hixview.web.controller.trad;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.hixview.domain.entity.home.BlogPost;
import site.hixview.domain.entity.home.dto.BlogPostDto;
import site.hixview.domain.service.BlogPostService;
import site.hixview.domain.service.CompanyService;
import site.hixview.domain.validation.validator.BlogPostAddValidator;
import site.hixview.domain.validation.validator.BlogPostModifyValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.ExceptionMessage.NO_BLOG_POST_WITH_THAT_NUMBER_OR_NAME;
import static site.hixview.domain.vo.Regex.NUMBER_PATTERN;
import static site.hixview.domain.vo.RequestUrl.*;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.manager.Layout.*;
import static site.hixview.domain.vo.manager.RequestURL.*;
import static site.hixview.domain.vo.manager.ViewName.*;
import static site.hixview.domain.vo.name.ExceptionName.NOT_FOUND_BLOG_POST_ERROR;
import static site.hixview.domain.vo.name.ViewName.*;
import static site.hixview.util.ControllerUtils.*;

@Controller
@RequiredArgsConstructor
public class ManagerBlogPostController {

    private final BlogPostService blogPostService;
    private final CompanyService companyService;

    private final BlogPostAddValidator addValidator;
    private final BlogPostModifyValidator modifyValidator;

    private final Logger log = LoggerFactory.getLogger(ManagerBlogPostController.class);

    /**
     * Add
     */
    @GetMapping(ADD_BLOG_POST_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processAddBlogPost(Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_PROCESS_LAYOUT);
        model.addAttribute(POST, new BlogPostDto());
        return ADD_BLOG_POST_VIEW + VIEW_PROCESS;
    }

    @GetMapping(ADD_BLOG_POST_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishAddBlogPost(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, ADD_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, ADD_BLOG_POST_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return ADD_BLOG_POST_VIEW + VIEW_FINISH;
    }

    /**
     * See
     */
    @GetMapping(SELECT_BLOG_POST_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processSeeBlogPosts(Model model) {
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(BLOG_POSTS, blogPostService.findPosts());
        return SELECT_VIEW + "blog-posts-page";
    }

    @GetMapping(CHECK_TARGET_IMAGE_PATH_BLOG_POST_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processCheckTargetImagePathBlogPosts(Model model) {
        List<BlogPost> postsWithInvalidImagePath = new ArrayList<>();
        Path prefix = Paths.get(ROOT_PATH).resolve(STATIC_RESOURCE_PATH);
        for (BlogPost post : blogPostService.findPosts()) {
            Path filePath = prefix
                    .resolve(post.getTargetImagePath().substring(1))
                    .toAbsolutePath();
            if (!filePath.toFile().isFile()) {
                postsWithInvalidImagePath.add(post);
            }
        }
        model.addAttribute(LAYOUT_PATH, SELECT_LAYOUT);
        model.addAttribute(BLOG_POSTS, postsWithInvalidImagePath);
        return SELECT_VIEW + "blog-posts-page";
    }

    /**
     * Modify
     */
    @GetMapping(UPDATE_BLOG_POST_URL)
	@ResponseStatus(HttpStatus.OK)
	public String initiateModifyBlogPost(Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_QUERY_LAYOUT);
		return UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS;
	}

    @PostMapping(UPDATE_BLOG_POST_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processModifyBlogPost(@RequestParam String numberOrName, Model model) {
        Optional<BlogPost> postOrEmpty = blogPostService.findPostByNumberOrName(numberOrName);
        if (postOrEmpty.isEmpty()) {
            finishForRollback(NO_BLOG_POST_WITH_THAT_NUMBER_OR_NAME,
                    UPDATE_QUERY_LAYOUT, NOT_FOUND_BLOG_POST_ERROR, model);
            return UPDATE_BLOG_POST_VIEW + VIEW_BEFORE_PROCESS;
        }

        model.addAttribute(LAYOUT_PATH, UPDATE_PROCESS_LAYOUT);
        model.addAttribute(UPDATE_URL, UPDATE_BLOG_POST_URL + FINISH_URL);
        model.addAttribute(POST, postOrEmpty.orElseThrow().toDto());
        return UPDATE_BLOG_POST_VIEW + VIEW_AFTER_PROCESS;
    }

    @GetMapping(UPDATE_BLOG_POST_URL + FINISH_URL)
	@ResponseStatus(HttpStatus.OK)
	public String finishModifyBlogPost(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, UPDATE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, UPDATE_BLOG_POST_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return UPDATE_BLOG_POST_VIEW + VIEW_FINISH;
	}

    /**
     * Get Rid of
     */
    @GetMapping(REMOVE_BLOG_POST_URL)
    @ResponseStatus(HttpStatus.OK)
    public String processRidBlogPost(Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_PROCESS_LAYOUT);
        return REMOVE_BLOG_POST_VIEW + VIEW_PROCESS;
    }

    @PostMapping(REMOVE_BLOG_POST_URL)
    public String submitRidBlogPost(@RequestParam String numberOrName, Model model) {
        Optional<BlogPost> postOrEmpty = blogPostService.findPostByNumberOrName(numberOrName);
        if (postOrEmpty.isEmpty()) {
            finishForRollback(NO_BLOG_POST_WITH_THAT_NUMBER_OR_NAME,
                    REMOVE_PROCESS_LAYOUT, NOT_FOUND_BLOG_POST_ERROR, model);
            return REMOVE_BLOG_POST_VIEW + VIEW_PROCESS;
        }

        if (NUMBER_PATTERN.matcher(numberOrName).matches()) {
            numberOrName = blogPostService.findPostByNumber(Long.parseLong(numberOrName)).orElseThrow().getName();
        }
        blogPostService.removePostByName(numberOrName);
        return REDIRECT_URL + fromPath(REMOVE_BLOG_POST_URL + FINISH_URL).queryParam(NAME, encodeWithUTF8(numberOrName)).build().toUriString();
    }

    @GetMapping(REMOVE_BLOG_POST_URL + FINISH_URL)
    @ResponseStatus(HttpStatus.OK)
    public String finishRidBlogPost(@RequestParam String name, Model model) {
        model.addAttribute(LAYOUT_PATH, REMOVE_FINISH_LAYOUT);
        model.addAttribute(REPEAT_URL, REMOVE_BLOG_POST_URL);
        model.addAttribute(VALUE, decodeWithUTF8(name));
        return REMOVE_BLOG_POST_VIEW + VIEW_FINISH;
    }
}
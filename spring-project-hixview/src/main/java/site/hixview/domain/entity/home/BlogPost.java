package site.hixview.domain.entity.home;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import site.hixview.domain.entity.Classification;
import site.hixview.domain.entity.home.dto.BlogPostDto;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static site.hixview.domain.vo.Regex.URL_REGEX;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.util.JsonUtils.deserializeWithOneMapToList;
import static site.hixview.util.JsonUtils.serializeWithOneMap;

@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlogPost {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Long number;

    @NotBlank(message = "{NotBlank.post.name}")
    @Size(max = 80, message = "{Size.post.name}")
    private String name;

    @NotBlank(message = "{NotBlank.post.link}")
    @Size(max = 400, message = "{Size.post.link}")
    @Pattern(regexp = URL_REGEX, message = "{Pattern.post.link}")
    private String link;

    @NotNull
    private LocalDate date;

    @NotNull
    private Classification classification;

    @NotBlank(message = "{NotBlank.post.targetName}")
    @Size(max = 80, message = "{Size.post.targetName}")
    private String targetName;

    @NotBlank(message = "{NotBlank.post.targetImagePath}")
    @Size(max = 80, message = "{Size.post.targetImagePath}")
    private String targetImagePath;

    @NotNull
    private List<String> targetArticleNames;

    @NotNull
    private List<String> targetArticleLinks;

    public String getSerializedTargetArticleNames() {
        return serializeWithOneMap(objectMapper, TARGET_ARTICLE_NAME, targetArticleNames);
    }

    public String getSerializedTargetArticleLinks() {
        return serializeWithOneMap(objectMapper, TARGET_ARTICLE_LINK, targetArticleLinks);
    }

    public BlogPostDto toDto() {
        BlogPostDto blogPostDto = new BlogPostDto();
        blogPostDto.setName(name);
        blogPostDto.setLink(link);
        blogPostDto.setYear(date.getYear());
        blogPostDto.setMonth(date.getMonthValue());
        blogPostDto.setDays(date.getDayOfMonth());
        blogPostDto.setClassification(classification.name());
        blogPostDto.setTargetName(targetName);
        blogPostDto.setTargetImagePath(targetImagePath);
        blogPostDto.setTargetArticleNames(getSerializedTargetArticleNames());
        blogPostDto.setTargetArticleLinks(getSerializedTargetArticleLinks());
        return blogPostDto;
    }

    public HashMap<String, Object> toMapWithNoNumber() {
        return new HashMap<>() {{
            put(NAME, name);
            put(LINK, link);
            put(DATE, date);
            put(CLASSIFICATION, classification);
            put(TARGET_NAME, targetName);
            put(TARGET_IMAGE_PATH, targetImagePath);
            put(TARGET_ARTICLE_NAMES, targetArticleNames);
            put(TARGET_ARTICLE_LINKS, targetArticleLinks);
        }};
    }

    public HashMap<String, Object> toSerializedMapWithNoNumber() {
        return new HashMap<>() {{
            putAll(toMapWithNoNumber());
            put(TARGET_ARTICLE_NAMES, getSerializedTargetArticleNames());
            put(TARGET_ARTICLE_LINKS, getSerializedTargetArticleLinks());
        }};
    }

    public static String[] getFieldNamesWithNoNumber() {
        return new String[]{NAME, LINK, DATE, CLASSIFICATION, TARGET_NAME, TARGET_IMAGE_PATH, TARGET_ARTICLE_NAMES, TARGET_ARTICLE_LINKS};
    }

    public static final class BlogPostBuilder {
        public BlogPostBuilder() {}

        public BlogPost.BlogPostBuilder blogPost(BlogPost blogPost) {
            name = blogPost.getName();
            link = blogPost.getLink();
            date = blogPost.getDate();
            classification = blogPost.getClassification();
            targetName = blogPost.getTargetName();
            targetImagePath = blogPost.getTargetImagePath();
            targetArticleNames = blogPost.getTargetArticleNames();
            targetArticleLinks = blogPost.getTargetArticleLinks();
            return this;
        }

        public BlogPost.BlogPostBuilder blogPostDto(BlogPostDto blogPostDto) {
            name = blogPostDto.getName();
            link = blogPostDto.getLink();
            date = LocalDate.of(blogPostDto.getYear(), blogPostDto.getMonth(), blogPostDto.getDays());
            classification = Classification.valueOf(blogPostDto.getClassification());
            targetName = blogPostDto.getTargetName();
            targetImagePath = blogPostDto.getTargetImagePath();
            targetArticleNames = deserializeWithOneMapToList(new ObjectMapper(), TARGET_ARTICLE_NAME, blogPostDto.getTargetArticleNames());
            targetArticleLinks = deserializeWithOneMapToList(new ObjectMapper(), TARGET_ARTICLE_LINK, blogPostDto.getTargetArticleLinks());
            return this;
        }
    }
}

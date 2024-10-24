package site.hixview.domain.entity.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import site.hixview.domain.validation.annotation.ClassificationConstraint;
import site.hixview.domain.validation.annotation.TargetArticleLinksConstraint;
import site.hixview.domain.validation.annotation.TargetArticleNamesConstraint;

import static site.hixview.domain.vo.Regex.URL_REGEX;

@Getter
@Setter
public class BlogPostDto {

    @NotBlank(message = "{NotBlank.post.name}")
    @Size(max = 80, message = "{Size.post.name}")
    private String name;

    @NotBlank(message = "{NotBlank.post.link}")
    @Size(max = 400, message = "{Size.post.link}")
    @Pattern(regexp = URL_REGEX, message = "{Pattern.post.link}")
    private String link;

    @NotNull(message = "{NotNull.post.year}")
    @Range(min = 2020, max = 2099, message = "{Range.post.year}")
    private Integer year;

    @NotNull(message = "{NotNull.post.month}")
    @Range(min = 1, max = 12, message = "{Range.post.month}")
    private Integer month;

    @NotNull(message = "{NotNull.post.days}")
    @Range(min = 1, max = 31, message = "{Range.post.days}")
    private Integer days;

    @ClassificationConstraint
    private String classification;

    @NotBlank(message = "{NotBlank.post.targetName}")
    @Size(max = 80, message = "{Size.post.targetName}")
    private String targetName;

    @NotBlank(message = "{NotBlank.post.targetImagePath}")
    @Size(max = 80, message = "{Size.post.targetImagePath}")
    private String targetImagePath;

    @TargetArticleNamesConstraint
    private String targetArticleNames;

    @TargetArticleLinksConstraint
    private String targetArticleLinks;
}

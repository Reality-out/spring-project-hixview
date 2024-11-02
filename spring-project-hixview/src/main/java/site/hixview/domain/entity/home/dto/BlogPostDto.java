package site.hixview.domain.entity.home.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import site.hixview.domain.validation.annotation.ClassificationConstraint;
import site.hixview.domain.validation.annotation.TargetArticleLinksConstraint;
import site.hixview.domain.validation.annotation.TargetArticleNamesConstraint;
import site.hixview.domain.validation.annotation.post.PostLink;
import site.hixview.domain.validation.annotation.post.PostName;
import site.hixview.domain.validation.annotation.post.PostTargetImagePath;
import site.hixview.domain.validation.annotation.post.PostTargetName;

@Getter
@Setter
public class BlogPostDto {

    @PostName
    private String name;

    @PostLink
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

    @PostTargetName
    private String targetName;

    @PostTargetImagePath
    private String targetImagePath;

    @TargetArticleNamesConstraint
    private String targetArticleNames;

    @TargetArticleLinksConstraint
    private String targetArticleLinks;
}

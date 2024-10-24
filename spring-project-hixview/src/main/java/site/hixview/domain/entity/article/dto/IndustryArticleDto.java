package site.hixview.domain.entity.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import site.hixview.domain.validation.annotation.*;

import static site.hixview.domain.vo.Regex.URL_REGEX;

@Getter
@Setter
public class IndustryArticleDto {
    @NotBlank(message = "{NotBlank.article.name}")
    @Size(max = 80, message = "{Size.article.name}")
    private String name;

    @PressConstraint
    private String press;

    @NotBlank(message = "{NotBlank.article.link}")
    @Size(max = 400, message = "{Size.article.link}")
    @Pattern(regexp = URL_REGEX, message = "{Pattern.article.link}")
    private String link;

    @NotNull(message = "{NotNull.article.year}")
    @Range(min = 1960, max = 2099, message = "{Range.article.year}")
    private Integer year;

    @NotNull(message = "{NotNull.article.month}")
    @Range(min = 1, max = 12, message = "{Range.article.month}")
    private Integer month;

    @NotNull(message = "{NotNull.article.days}")
    @Range(min = 1, max = 31, message = "{Range.article.days}")
    private Integer days;

    @ImportanceConstraint
    private Integer importance;

    @SubjectFirstCategoryConstraint
    private String subjectFirstCategory;

    @SubjectSecondCategoriesConstraint
    private String subjectSecondCategories;
}

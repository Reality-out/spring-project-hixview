package site.hixview.domain.entity.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import site.hixview.domain.validation.annotation.FirstCategoryConstraint;
import site.hixview.domain.validation.annotation.ImportanceConstraint;
import site.hixview.domain.validation.annotation.PressConstraint;
import site.hixview.domain.validation.annotation.SecondCategoriesConstraint;

import static site.hixview.domain.vo.Regex.URL_REGEX;

@Getter
@Setter
public class IndustryArticleDto {
    @NotBlank
    @Size(max = 80)
    private String name;

    @PressConstraint
    private String press;

    @NotBlank
    @Size(max = 400)
    @Pattern(regexp = URL_REGEX)
    private String link;

    @NotNull
    @Range(min = 1960, max = 2099)
    private Integer year;

    @NotNull
    @Range(min = 1, max = 12)
    private Integer month;

    @NotNull
    @Range(min = 1, max = 31)
    private Integer days;

    @ImportanceConstraint
    private Integer importance;

    @FirstCategoryConstraint
    private String subjectFirstCategory;

    @SecondCategoriesConstraint
    private String subjectSecondCategories;
}

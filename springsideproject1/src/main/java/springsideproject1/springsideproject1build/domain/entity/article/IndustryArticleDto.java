package springsideproject1.springsideproject1build.domain.entity.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import springsideproject1.springsideproject1build.domain.validation.annotation.FirstCategoryConstraint;
import springsideproject1.springsideproject1build.domain.validation.annotation.ImportanceConstraint;
import springsideproject1.springsideproject1build.domain.validation.annotation.PressConstraint;
import springsideproject1.springsideproject1build.domain.validation.annotation.SecondCategoryConstraint;

import static springsideproject1.springsideproject1build.domain.vo.REGEX.URL_REGEX;

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

    @SecondCategoryConstraint
    private String subjectSecondCategory;
}

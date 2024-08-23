package springsideproject1.springsideproject1build.domain.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import static springsideproject1.springsideproject1build.vo.REGEX.URL_REGEX;

@Getter
@Setter
public class CompanyArticleDto {

    @NotBlank
    private String name;

    @NotBlank
    private String press;

    @NotBlank
    private String subjectCompany;

    @NotBlank
    @Pattern(regexp = URL_REGEX)
    private String link;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;

    @NotNull
    private Integer date;

    @NotNull
    private Integer importance;

}

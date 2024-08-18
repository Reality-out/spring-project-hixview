package springsideproject1.springsideproject1build.domain.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CompanyArticleDto {

    private Long number;

    @NotBlank
    private String name;

    @NotBlank
    private String press;

    @NotBlank
    private String subjectCompany;

    @NotBlank
    @URL
    private String link;

    @NotBlank
    private String year;

    @NotBlank
    private String month;

    @NotBlank
    private String date;

    @NotNull
    private Integer importance;
}
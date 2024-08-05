package springsideproject1.springsideproject1build.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyArticleDto {
    private Long number;
    private String name;
    private String press;
    private String subjectCompany;
    private String link;
    private String year;
    private String month;
    private String date;
    private Integer importance;
}
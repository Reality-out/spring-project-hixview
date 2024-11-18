package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDtoNoNumber {
    private String name;
    private String link;
    private String date;
    private String classification;
    private String subjectCountry;
    private String importance;
    private String summary;
    private Long pressNumber;
}
package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EconomyArticleDtoNoNumber {
    private String name;
    private String link;
    private String date;
    private String subjectCountry;
    private String importance;
    private String summary;
    private Long pressNumber;
    private String mappedEconomyContentNumbers;
}
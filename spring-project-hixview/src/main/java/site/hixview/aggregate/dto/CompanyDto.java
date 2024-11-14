package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {
    private String code;
    private String koreanName;
    private String englishName;
    private String nameListed;
    private String countryListed;
    private String scale;
    private Long firstCategoryNumber;
    private Long secondCategoryNumber;
}

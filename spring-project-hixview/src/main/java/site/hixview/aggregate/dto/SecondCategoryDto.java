package site.hixview.aggregate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecondCategoryDto {
    private Long number;
    private String koreanName;
    private String englishName;
    private Long industryCategoryNumber;
    private Long firstCategoryNumber;
}
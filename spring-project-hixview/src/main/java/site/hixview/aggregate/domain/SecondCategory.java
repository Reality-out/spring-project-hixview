package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SecondCategory {
    private final Long number;
    private final String koreanName;
    private final String englishName;
    private final Long industryCategoryNumber;
    private final Long firstCategoryNumber;
}

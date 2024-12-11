package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Scale;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class Company {
    private final String code;
    private final String koreanName;
    private final String englishName;
    private final String nameListed;
    private final Country countryListed;
    private final Scale scale;
    private final Long firstCategoryNumber;
    private final Long secondCategoryNumber;
}

package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticle {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Country subjectCountry;
    private final Importance importance;
    private final String summary;
    private final Long pressNumber;
    private final List<String> mappedCompanyCodes;
}

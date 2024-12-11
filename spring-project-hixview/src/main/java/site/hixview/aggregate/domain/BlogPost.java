package site.hixview.aggregate.domain;

import lombok.*;
import site.hixview.aggregate.enums.Classification;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class BlogPost {
    private final Long number;
    private final String name;
    private final String link;
    private final LocalDate date;
    private final Classification classification;
    private final List<Long> mappedArticleNumbers;
}

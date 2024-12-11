package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PUBLIC)
public class Press {
    private final Long number;
    private final String koreanName;
    private final String englishName;
}

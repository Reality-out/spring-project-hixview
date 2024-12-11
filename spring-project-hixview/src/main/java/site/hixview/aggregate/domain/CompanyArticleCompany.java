package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class CompanyArticleCompany {
    private final Long number;
    private final Long articleNumber;
    private final String companyCode;
}

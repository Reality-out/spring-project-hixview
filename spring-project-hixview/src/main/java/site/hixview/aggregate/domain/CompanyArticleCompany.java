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

    public static class CompanyArticleCompanyBuilder {
        private Long number;
        private Long articleNumber;
        private String companyCode;

        public CompanyArticleCompanyBuilder companyArticleCompany(final CompanyArticleCompany companyArticleCompany) {
            this.number = companyArticleCompany.getNumber();
            this.articleNumber = companyArticleCompany.getArticleNumber();
            this.companyCode = companyArticleCompany.getCompanyCode();
            return this;
        }

        public CompanyArticleCompany build() {
            return new CompanyArticleCompany(this.number, this.articleNumber, this.companyCode);
        }
    }
}

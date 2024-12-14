package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class IndustryArticleSecondCategory {
    private final Long number;
    private final Long articleNumber;
    private final Long secondCategoryNumber;

    public static class IndustryArticleSecondCategoryBuilder {
        private Long number;
        private Long articleNumber;
        private Long secondCategoryNumber;

        public IndustryArticleSecondCategoryBuilder industryArticleSecondCategory(final IndustryArticleSecondCategory industryArticleSecondCategory) {
            this.number = industryArticleSecondCategory.getNumber();
            this.articleNumber = industryArticleSecondCategory.getArticleNumber();
            this.secondCategoryNumber = industryArticleSecondCategory.getSecondCategoryNumber();
            return this;
        }

        public IndustryArticleSecondCategory build() {
            return new IndustryArticleSecondCategory(this.number, this.articleNumber, this.secondCategoryNumber);
        }
    }
}

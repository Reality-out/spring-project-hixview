package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyArticleContent {
    private final Long number;
    private final Long articleNumber;
    private final Long contentNumber;

    public static class EconomyArticleContentBuilder {
        private Long number;
        private Long articleNumber;
        private Long contentNumber;

        public EconomyArticleContentBuilder economyArticleContent(final EconomyArticleContent economyArticleContent) {
            this.number = economyArticleContent.getNumber();
            this.articleNumber = economyArticleContent.getArticleNumber();
            this.contentNumber = economyArticleContent.getContentNumber();
            return this;
        }

        public EconomyArticleContent build() {
            return new EconomyArticleContent(this.number, this.articleNumber, this.contentNumber);
        }
    }
}

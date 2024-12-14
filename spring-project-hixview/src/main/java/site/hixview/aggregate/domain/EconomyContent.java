package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class EconomyContent {
    private final Long number;
    private final String name;

    public static class EconomyContentBuilder {
        private Long number;
        private String name;

        public EconomyContentBuilder economyContent(final EconomyContent economyContent) {
            this.number = economyContent.getNumber();
            this.name = economyContent.getName();
            return this;
        }

        public EconomyContent build() {
            return new EconomyContent(this.number, this.name);
        }
    }
}

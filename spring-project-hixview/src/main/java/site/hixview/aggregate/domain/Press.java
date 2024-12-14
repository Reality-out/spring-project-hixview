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

    public static class PressBuilder {
        private Long number;
        private String koreanName;
        private String englishName;

        public Press.PressBuilder press(final Press press) {
            this.number = press.getNumber();
            this.koreanName = press.getKoreanName();
            this.englishName = press.getEnglishName();
            return this;
        }

        public Press build() {
            return new Press(this.number, this.koreanName, this.englishName);
        }
    }
}

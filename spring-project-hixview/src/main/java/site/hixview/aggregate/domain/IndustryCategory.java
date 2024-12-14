package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PUBLIC)
public class IndustryCategory {
    private final Long number;
    private final String koreanName;
    private final String englishName;

    public static class IndustryCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;

        public IndustryCategoryBuilder industryCategory(final IndustryCategory industryCategory) {
            this.number = industryCategory.getNumber();
            this.koreanName = industryCategory.getKoreanName();
            this.englishName = industryCategory.getEnglishName();
            return this;
        }

        public IndustryCategory build() {
            return new IndustryCategory(this.number, this.koreanName, this.englishName);
        }
    }
}

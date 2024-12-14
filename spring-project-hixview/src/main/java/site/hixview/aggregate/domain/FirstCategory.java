package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class FirstCategory {
    private final Long number;
    private final String koreanName;
    private final String englishName;
    private final Long industryCategoryNumber;

    public static class FirstCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;
        private Long industryCategoryNumber;

        public FirstCategoryBuilder firstCategory(final FirstCategory firstCategory) {
            this.number = firstCategory.getNumber();
            this.koreanName = firstCategory.getKoreanName();
            this.englishName = firstCategory.getEnglishName();
            this.industryCategoryNumber = firstCategory.getIndustryCategoryNumber();
            return this;
        }

        public FirstCategory build() {
            return new FirstCategory(this.number, this.koreanName, this.englishName, this.industryCategoryNumber);
        }
    }
}

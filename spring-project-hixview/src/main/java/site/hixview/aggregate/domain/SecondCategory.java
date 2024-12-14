package site.hixview.aggregate.domain;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PUBLIC)
public class SecondCategory {
    private final Long number;
    private final String koreanName;
    private final String englishName;
    private final Long industryCategoryNumber;
    private final Long firstCategoryNumber;

    public static class SecondCategoryBuilder {
        private Long number;
        private String koreanName;
        private String englishName;
        private Long industryCategoryNumber;
        private Long firstCategoryNumber;

        public SecondCategoryBuilder secondCategory(final SecondCategory secondCategory) {
            this.number = secondCategory.getNumber();
            this.koreanName = secondCategory.getKoreanName();
            this.englishName = secondCategory.getEnglishName();
            this.industryCategoryNumber = secondCategory.getIndustryCategoryNumber();
            this.firstCategoryNumber = secondCategory.getFirstCategoryNumber();
            return this;
        }

        public SecondCategory build() {
            return new SecondCategory(this.number, this.koreanName, this.englishName, this.industryCategoryNumber, this.firstCategoryNumber);
        }
    }
}
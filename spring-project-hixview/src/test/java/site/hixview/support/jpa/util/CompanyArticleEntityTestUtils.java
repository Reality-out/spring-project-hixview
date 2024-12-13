package site.hixview.support.jpa.util;

import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.CompanyArticleEntity;

import java.time.LocalDate;

public interface CompanyArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils {
    /**
     * Create
     */
    default CompanyArticleEntity createCompanyArticleEntity() {
        return CompanyArticleEntity.builder()
                .article(createArticleEntity())
                .name("[단독] 파나소닉 &quot;현대차와 언제든 전장협업 가능&quot")
                .link("https://www.sedaily.com/NewsView/2D413KUMZY")
                .date(LocalDate.of(2024, 1, 10))
                .subjectCountry(Country.SOUTH_KOREA.name())
                .importance(Importance.MODERATE.name())
                .summary("테스트용 첫 번째 기업 기사")
                .press(createPressEntity())
                .build();
    }

    default CompanyArticleEntity createAnotherCompanyArticleEntity() {
        return CompanyArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name("원료부터 재활용 기술 확보까지…현대차그룹, 배터리 내재화 이유는%3F")
                .link("https://www.newswatch.kr/news/articleView.html?idxno=66894")
                .date(LocalDate.of(2024, 1, 23))
                .subjectCountry(Country.SOUTH_KOREA.name())
                .importance(Importance.MODERATE.name())
                .summary("테스트용 두 번째 기업 기사")
                .press(createAnotherPressEntity())
                .build();
    }
}

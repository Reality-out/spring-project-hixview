package site.hixview.support.jpa.util;

import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.EconomyArticleEntity;

import java.time.LocalDate;

public interface EconomyArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils {
    /**
     * Create
     */
    default EconomyArticleEntity createEconomyArticleEntity() {
        return EconomyArticleEntity.builder()
                .article(createArticleEntity())
                .name("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입")
                .link("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257")
                .date(LocalDate.of(2024, 9, 30))
                .subjectCountry(Country.CHINA.name())
                .importance(Importance.MODERATE.name())
                .summary("테스트용 첫 번째 경제 기사")
                .press(createPressEntity())
                .build();
    }

    default EconomyArticleEntity createAnotherEconomyArticleEntity() {
        return EconomyArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처")
                .link("https://www.newsis.com/view/NISX20241009_0002913996")
                .date(LocalDate.of(2024, 10, 9))
                .subjectCountry(Country.SOUTH_KOREA.name())
                .importance(Importance.MODERATE.name())
                .summary("테스트용 두 번째 경제 기사")
                .press(createAnotherPressEntity())
                .build();
    }
}

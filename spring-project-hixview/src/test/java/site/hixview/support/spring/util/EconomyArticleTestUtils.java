package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.EconomyArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

public interface EconomyArticleTestUtils {
    EconomyArticle economyArticle = EconomyArticle.builder()
            .number(1L)
            .name("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입")
            .link("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257")
            .date(LocalDate.of(2024, 9, 30))
            .subjectCountry(Country.CHINA)
            .importance(Importance.MODERATE)
            .summary("테스트용 첫 번째 경제 기사")
            .pressNumber(1L)
            .mappedEconomyContentNumbers(List.of(1L, 2L))
            .build();

    EconomyArticle anotherEconomyArticle = EconomyArticle.builder()
            .number(2L)
            .name("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처")
            .link("https://www.newsis.com/view/NISX20241009_0002913996")
            .date(LocalDate.of(2024, 10, 9))
            .subjectCountry(Country.SOUTH_KOREA)
            .importance(Importance.MODERATE)
            .summary("테스트용 두 번째 경제 기사")
            .pressNumber(2L)
            .mappedEconomyContentNumbers(List.of(3L, 4L))
            .build();
}

package site.hixview.support.util;

import site.hixview.aggregate.enums.Classification;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.ArticleEntity;

import java.time.LocalDate;

public interface ArticleTestUtils extends PressTestUtils {
    /**
     * Create
     */
    default ArticleEntity createArticle(){
        return ArticleEntity.builder()
                .name("“돈 푼다” 공언에 돈 들어왔다, 中 증시 나흘새 8조원 순유입")
                .link("https://m.edaily.co.kr/News/Read?newsId=03499766639025368&mediaCodeNo=257")
                .date(LocalDate.of(2024, 9, 30))
                .classification(Classification.ECONOMY.name())
                .subjectCountry(Country.CHINA.name())
                .importance(Importance.MODERATE.name())
                .summary("세계국채지수에 9번째 규모로 편입된 우리나라 채권 시장")
                .press(createPress())
                .build();
    }
    //    		14
    default ArticleEntity createAnotherArticle(){
        return ArticleEntity.builder()
                .name("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처")
                .link("https://www.newsis.com/view/NISX20241009_0002913996")
                .date(LocalDate.of(2024, 10, 9))
                .classification(Classification.ECONOMY.name())
                .subjectCountry(Country.SOUTH_KOREA.name())
                .importance(Importance.MODERATE.name())
                .summary("세계국채지수에 9번째 규모로 편입된 우리나라 채권 시장")
                .press(createAnotherPress())
                .build();
    }
}

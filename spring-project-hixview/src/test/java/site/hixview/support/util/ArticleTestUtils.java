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
    default ArticleEntity createTestArticle(){
        return ArticleEntity.builder()
                .name("韓, WGBI 26번째 편입…9번째 규모 글로벌 투자처")
                .link("https://www.newsis.com/view/NISX20241009_0002913996")
                .date(LocalDate.of(2024, 10, 9))
                .classification(Classification.ECONOMY.name())
                .subjectCountry(Country.SOUTH_KOREA.name())
                .importance(Importance.MODERATE.name())
                .summary("세계국채지수에 9번째 규모로 편입된 우리나라 채권 시장")
                .press(createTestPress())
                .build();
    }
}

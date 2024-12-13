package site.hixview.support.jpa.util;

import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;
import site.hixview.jpa.entity.IndustryArticleEntity;

import java.time.LocalDate;

public interface IndustryArticleEntityTestUtils extends ArticleEntityTestUtils, PressEntityTestUtils, FirstCategoryEntityTestUtils {
    /**
     * Create
     */
    default IndustryArticleEntity createIndustryArticleEntity() {
        return IndustryArticleEntity.builder()
                .article(createArticleEntity())
                .name("ASML, 중국 내 장비 유지보수 중단… &quot;中, 반도체 산업 타격 불가피&quot;")
                .link("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/")
                .date(LocalDate.of(2024, 8, 30))
                .subjectCountry(Country.CHINA.name())
                .importance(Importance.IMPORTANT.name())
                .summary("테스트용 첫 번째 산업 기사")
                .press(createPressEntity())
                .firstCategory(createFirstCategoryEntity())
                .build();
    }

    default IndustryArticleEntity createAnotherIndustryArticleEntity() {
        return IndustryArticleEntity.builder()
                .article(createAnotherArticleEntity())
                .name("흔들리는 반도체 굴기…화웨이 &quot;3·5나노칩 확보 불가&quot;")
                .link("https://www.sedaily.com/NewsView/2DAECLJTW4")
                .date(LocalDate.of(2024, 6, 9))
                .subjectCountry(Country.CHINA.name())
                .importance(Importance.MODERATE.name())
                .summary("테스트용 두 번째 기업 기사")
                .press(createAnotherPressEntity())
                .firstCategory(createAnotherFirstCategoryEntity())
                .build();
    }
}

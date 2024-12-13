package site.hixview.support.spring.util;

import site.hixview.aggregate.domain.IndustryArticle;
import site.hixview.aggregate.enums.Country;
import site.hixview.aggregate.enums.Importance;

import java.time.LocalDate;
import java.util.List;

public interface IndustryArticleTestUtils {
    IndustryArticle industryArticle = IndustryArticle.builder()
            .number(1L)
            .name("ASML, 중국 내 장비 유지보수 중단… &quot;中, 반도체 산업 타격 불가피&quot;")
            .link("https://biz.chosun.com/it-science/ict/2024/08/30/EFI7ZQXPTZE45D53WKE4DNQ6AU/")
            .date(LocalDate.of(2024, 8, 30))
            .subjectCountry(Country.CHINA)
            .importance(Importance.IMPORTANT)
            .summary("테스트용 첫 번째 산업 기사")
            .pressNumber(1L)
            .mappedSecondCategoryNumbers(List.of(1L, 2L))
            .build();

    IndustryArticle anotherIndustryArticle = IndustryArticle.builder()
            .number(2L)
            .name("흔들리는 반도체 굴기…화웨이 &quot;3·5나노칩 확보 불가&quot;")
            .link("https://www.sedaily.com/NewsView/2DAECLJTW4")
            .date(LocalDate.of(2024, 6, 9))
            .subjectCountry(Country.CHINA)
            .importance(Importance.MODERATE)
            .summary("테스트용 두 번째 산업 기사")
            .pressNumber(2L)
            .mappedSecondCategoryNumbers(List.of(3L, 4L))
            .build();
}

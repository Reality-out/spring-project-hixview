package springsideproject1.springsideproject1build.utility.test;

import springsideproject1.springsideproject1build.domain.CompanyArticle;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public interface CompanyArticleTest extends ObjectTest {

    String articleTable = "testarticles";

    default CompanyArticle createTestArticle() {
        return new CompanyArticle.ArticleBuilder()
                .name("'OLED 위기감' 삼성디스플레이, 주64시간제 도입…삼성 비상경영 확산")
                .press("SBS")
                .subjectCompany("삼성디스플레이")
                .link("https://biz.sbs.co.kr/article/20000176881")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    default CompanyArticle createTestEqualDateArticle() {
        return new CompanyArticle.ArticleBuilder()
                .name("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회")
                .press("비즈니스포스트")
                .subjectCompany("삼성전자")
                .link("https://www.businesspost.co.kr/BP?command=article_view&num=355822")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    default CompanyArticle createTestNewArticle() {
        return new CompanyArticle.ArticleBuilder()
                .name("[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치")
                .press("헤럴드경제")
                .subjectCompany("삼성전자")
                .link("https://biz.heraldcorp.com/view.php?ud=20240617050050")
                .date(LocalDate.of(2024, 6, 17))
                .importance(0)
                .build();
    }

    // These codes consist of two contents, createTestEqualDateArticle and createTestNewArticle
    default List<String> createTestStringArticle() {
        return Arrays.asList("삼성전자",
                String.join(System.lineSeparator(),
                        Arrays.asList("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회",
                                "(2024-6-18, 비즈니스포스트)",
                                "[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치",
                                "(2024-6-17, 헤럴드경제)")),
                String.join(System.lineSeparator(),
                        Arrays.asList("https://www.businesspost.co.kr/BP?command=article_view&num=355822",
                                "https://biz.heraldcorp.com/view.php?ud=20240617050050")));
    }
}

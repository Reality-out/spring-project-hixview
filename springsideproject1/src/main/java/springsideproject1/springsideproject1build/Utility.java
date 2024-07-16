package springsideproject1.springsideproject1build;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springsideproject1.springsideproject1build.domain.Article;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.domain.Member;

import java.time.LocalDate;
import java.util.regex.Pattern;

@UtilityClass
public class Utility {
    /**
     * Constant
     */
    public static final String articleTable = "testarticles";
    public static final String companyTable = "testcompanies";
    public static final String memberTable = "testmembers";

    /**
     * RowMapper
     */
    public static RowMapper<Article> articleRowMapper() {
        return (resultSet, rowNumber) -> {
            Article article = new Article.ArticleBuilder()
                    .number(resultSet.getLong("number"))
                    .name(resultSet.getString("name"))
                    .press(resultSet.getString("press"))
                    .subjectCompany(resultSet.getString("subjectcompany"))
                    .link(resultSet.getString("link"))
                    .date(resultSet.getDate("date").toLocalDate())
                    .importance(resultSet.getInt("importance"))
                    .build();
            return article;
        };
    }

    public static RowMapper<Company> companyRowMapper() {
        return (resultSet, rowNumber) -> {
            Company company = new Company.CompanyBuilder()
                    .code(resultSet.getString("code"))
                    .country(resultSet.getString("country"))
                    .scale(resultSet.getString("scale"))
                    .name(resultSet.getString("name"))
                    .category1st(resultSet.getString("category1st"))
                    .category2nd(resultSet.getString("category2nd"))
                    .build();
            return company;
        };
    }

    public static RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) -> {
            Member member = new Member.MemberBuilder()
                    .identifier(resultSet.getLong("identifier"))
                    .id(resultSet.getString("id"))
                    .password(resultSet.getString("password"))
                    .name(resultSet.getString("name"))
                    .build();
            return member;
        };
    }

    /**
     * Test
     */
    // Article
    public static Article createTestArticle() {
        return new Article.ArticleBuilder()
                .name("'OLED 위기감' 삼성디스플레이, 주64시간제 도입…삼성 비상경영 확산")
                .press("SBS")
                .subjectCompany("삼성디스플레이")
                .link("https://biz.sbs.co.kr/article/20000176881")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    public static Article createTestEqualDateArticle() {
        return new Article.ArticleBuilder()
                .name("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회")
                .press("비즈니스포스트")
                .subjectCompany("삼성전자")
                .link("https://www.businesspost.co.kr/BP?command=article_view&num=355822")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    public static Article createTestNewArticle() {
        return new Article.ArticleBuilder()
                .name("[단독] 삼성전자 네트워크사업부 인력 700명 전환 배치")
                .press("헤럴드경제")
                .subjectCompany("삼성전자")
                .link("https://biz.heraldcorp.com/view.php?ud=20240617050050")
                .date(LocalDate.of(2024, 6, 17))
                .importance(0)
                .build();
    }

    // Company
    public static Company createSamsungElectronics() {
        return new Company.CompanyBuilder()
                .code("005930")
                .country("South Korea")
                .scale("big")
                .name("삼성전자")
                .category1st("electronics")
                .category2nd("semiconductor")
                .build();
    }

    public static Company createSKHynix() {
        return new Company.CompanyBuilder()
                .code("000660")
                .country("South Korea")
                .scale("big")
                .name("SK하이닉스")
                .category1st("electronics")
                .category2nd("semiconductor")
                .build();
    }

    // Member
    public static Member createTestMember() {
        return new Member.MemberBuilder()
                .identifier(1L)
                .id("ABcd1234!")
                .password("EFgh1234!")
                .name("박진하")
                .build();
    }

    public static Member createTestNewMember() {
        return new Member.MemberBuilder()
                .identifier(2L)
                .id("abCD4321!")
                .password("OPqr4321!")
                .name("박하진")
                .build();
    }

    // General-Purpose
    public static void resetTable(JdbcTemplate jdbcTemplateTest, String tableName) {
        resetTable(jdbcTemplateTest, tableName, false);
    }

    public static void resetTable(JdbcTemplate jdbcTemplateTest, String tableName, boolean hasAutoIncrement) {
        jdbcTemplateTest.execute("DELETE FROM " + tableName);
        if (hasAutoIncrement) {
            jdbcTemplateTest.execute("ALTER TABLE " + tableName + " AUTO_INCREMENT = 1");
        }
    }

    /**
     * Validation
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}

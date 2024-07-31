package springsideproject1.springsideproject1build;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.domain.CompanyArticle;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.repository.CompanyArticleRepository;
import springsideproject1.springsideproject1build.repository.CompanyRepository;
import springsideproject1.springsideproject1build.repository.MemberRepository;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static springsideproject1.springsideproject1build.config.constant.EXCEPTION_MESSAGE_CONFIG.*;

@UtilityClass
@Transactional
public class Utility {
    /**
     * Constant
     */

    // DB Table Names
    public static final String articleTable = "testarticles";
    public static final String companyTable = "testcompanies";
    public static final String memberTable = "testmembers";

    /**
     * Convert
     */
    public static String toStringForUrl(List<?> list) {
        Pattern pattern = Pattern.compile("[\\[\\] ]"); // Regex pattern for [, ], and space
        return Pattern.compile("").splitAsStream(list.toString())
                .filter(c -> !pattern.matcher(c).matches())
                .collect(Collectors.joining());
    }

    /**
     * Decode
     */
    public static List<String> decodeUTF8(List<String> list) {
        list.replaceAll(s -> URLDecoder.decode(s, StandardCharsets.UTF_8));
        return list;
    }

    /**
     * Encode
     */
    public static List<String> encodeUTF8(List<String> list) {
        list.replaceAll(s -> URLEncoder.encode(s, StandardCharsets.UTF_8));
        return list;
    }

    /**
     * RowMapper
     */
    public static RowMapper<CompanyArticle> articleRowMapper() {
        return (resultSet, rowNumber) ->
                new CompanyArticle.ArticleBuilder()
                .number(resultSet.getLong("number"))
                .name(resultSet.getString("name"))
                .press(resultSet.getString("press"))
                .subjectCompany(resultSet.getString("subjectcompany"))
                .link(resultSet.getString("link"))
                .date(resultSet.getDate("date").toLocalDate())
                .importance(resultSet.getInt("importance"))
                .build();
    }

    public static RowMapper<Company> companyRowMapper() {
        return (resultSet, rowNumber) ->
                new Company.CompanyBuilder()
                .code(resultSet.getString("code"))
                .country(resultSet.getString("country"))
                .scale(resultSet.getString("scale"))
                .name(resultSet.getString("name"))
                .category1st(resultSet.getString("category1st"))
                .category2nd(resultSet.getString("category2nd"))
                .build();
    }

    public static RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) ->
                new Member.MemberBuilder()
                .identifier(resultSet.getLong("identifier"))
                .id(resultSet.getString("id"))
                .password(resultSet.getString("password"))
                .name(resultSet.getString("name"))
                .build();
    }

    /**
     * Test
     */

    // CompanyArticle
    public static CompanyArticle createTestArticle() {
        return new CompanyArticle.ArticleBuilder()
                .name("'OLED 위기감' 삼성디스플레이, 주64시간제 도입…삼성 비상경영 확산")
                .press("SBS")
                .subjectCompany("삼성디스플레이")
                .link("https://biz.sbs.co.kr/article/20000176881")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    public static CompanyArticle createTestEqualDateArticle() {
        return new CompanyArticle.ArticleBuilder()
                .name("삼성전자도 현대차 이어 인도법인 상장 가능성, '코리아 디스카운트' 해소 기회")
                .press("비즈니스포스트")
                .subjectCompany("삼성전자")
                .link("https://www.businesspost.co.kr/BP?command=article_view&num=355822")
                .date(LocalDate.of(2024, 6, 18))
                .importance(0)
                .build();
    }

    public static CompanyArticle createTestNewArticle() {
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
    public static List<String> createTestStringArticle() {
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
                .id("ABcd1234!")
                .password("EFgh1234!")
                .name("박진하")
                .build();
    }

    public static Member createTestNewMember() {
        return new Member.MemberBuilder()
                .id("abCD4321!")
                .password("OPqr4321!")
                .name("박하진")
                .build();
    }

    // BeforeEach-Only
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
     * Validate
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }

    @Transactional
    public static void duplicateCheck(CompanyArticleRepository repository, CompanyArticle article) {
        repository.searchArticleByName(article.getName()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_ARTICLE_NAME);}
        );
    }

    public static void duplicateCheck(CompanyRepository repository, Company company) {
        repository.searchCompanyByCode(company.getCode()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_COMPANY_CODE);}
        );
    }

    @Transactional
    public static void duplicateCheck(MemberRepository repository, Member member) {
        repository.findMemberByID(member.getId()).ifPresent(
                v -> {throw new IllegalStateException(ALREADY_EXIST_MEMBER_ID);}
        );
    }
}

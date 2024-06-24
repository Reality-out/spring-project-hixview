package springsideproject1.springsideproject1build;

import lombok.experimental.UtilityClass;
import org.springframework.jdbc.core.RowMapper;
import springsideproject1.springsideproject1build.domain.Company;
import springsideproject1.springsideproject1build.domain.Member;

import java.util.regex.Pattern;

@UtilityClass
public class Utility {
    /**
     * RowMapper
     */
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

    /**
     * Validation
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}

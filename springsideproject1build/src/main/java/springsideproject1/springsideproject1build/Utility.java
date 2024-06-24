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
            Company company = new Company();
            company.setCode(resultSet.getString("code"));
            company.setCountry(resultSet.getString("country"));
            company.setScale(resultSet.getString("scale"));
            company.setName(resultSet.getString("name"));
            company.setCategory1st(resultSet.getString("category1st"));
            company.setCategory2nd(resultSet.getString("category2nd"));
            return company;
        };
    }

    public static RowMapper<Member> memberRowMapper() {
        return (resultSet, rowNumber) -> {
            Member member = new Member();
            member.setIdentifier(resultSet.getLong("identifier"));
            member.setId(resultSet.getString("id"));
            member.setPassword(resultSet.getString("password"));
            member.setName(resultSet.getString("name"));
            return member;
        };
    }

    /**
     * Validation
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("[0-9]+", string);
    }
}

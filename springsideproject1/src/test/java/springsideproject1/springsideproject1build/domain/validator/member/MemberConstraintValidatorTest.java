package springsideproject1.springsideproject1build.domain.validator.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.TEST_MEMBER_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.MEMBERSHIP_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberConstraintValidatorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberConstraintValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_MEMBER_TABLE, true);
    }

    @DisplayName("date의 Restrict에 대한 회원 가입 유효성 검증")
    @Test
    public void validateDateRestrictMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setYear(2099);
        memberDto.setMonth(1);
        memberDto.setDays(1);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("date의 TypeButInvalid에 대한 회원 가입 유효성 검증")
    @Test
    public void validateDateTypeButInvalidMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setYear(2000);
        memberDto.setMonth(2);
        memberDto.setDays(31);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }
}

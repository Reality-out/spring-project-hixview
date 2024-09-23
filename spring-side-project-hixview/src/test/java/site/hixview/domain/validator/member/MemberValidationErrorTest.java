package site.hixview.domain.validator.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.member.MemberDto;
import site.hixview.domain.service.MemberService;
import site.hixview.util.test.MemberTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.name.EntityName.Member.MEMBER;
import static site.hixview.domain.vo.name.SchemaName.TEST_MEMBERS_SCHEMA;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;

@SpringBootTest(properties = "junit.jupiter.execution.parallel.mode.classes.default=same_thread")
@AutoConfigureMockMvc
@Transactional
public class MemberValidationErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberValidationErrorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, TEST_MEMBERS_SCHEMA, true);
    }

    @DisplayName("미래의 생일을 사용하는 회원 가입")
    @Test
    public void futureBirthMembership() throws Exception {
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

    @DisplayName("유효하지 않은 생일을 사용하는 회원 가입")
    @Test
    public void invalidBirthMembership() throws Exception {
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

package springsideproject1.springsideproject1build.domain.validator.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import javax.sql.DataSource;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ID;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.MEMBER_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.MEMBERSHIP_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberDefaultValidatorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public MemberDefaultValidatorTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, MEMBER_TABLE, true);
    }

    @DisplayName("NotBlank(공백)에 대한 회원 가입 유효성 검증")
    @Test
    public void validateNotBlankSpaceMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setId(" ");
        memberDto.setPassword(" ");
        memberDto.setName(" ");
        memberDto.setPhoneNumber(" ");

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("NotBlank(null)에 대한 회원 가입 유효성 검증")
    @Test
    public void validateNotBlankNullMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setId(null);
        memberDto.setPassword(null);
        memberDto.setName(null);
        memberDto.setPhoneNumber(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("NotNull에 대한 회원 가입 유효성 검증")
    @Test
    public void validateNotNullMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setYear(null);
        memberDto.setMonth(null);
        memberDto.setDays(null);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("Pattern에 대한 회원 가입 유효성 검증")
    @Test
    public void validatePatternMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setId(INVALID_VALUE);
        memberDto.setPassword(INVALID_VALUE);
        memberDto.setPhoneNumber(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("typeMismatch에 대한 회원 가입 유효성 검증")
    @Test
    public void validateTypeMismatchMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();

        // then
        requireNonNull(mockMvc.perform(post(MEMBERSHIP_URL).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param(ID, memberDto.getId())
                        .param("password", memberDto.getPassword())
                        .param(NAME, memberDto.getName())
                        .param(YEAR, INVALID_VALUE)
                        .param(MONTH, INVALID_VALUE)
                        .param(DAYS, INVALID_VALUE)
                        .param("phoneNumber", memberDto.getPhoneNumber()))
                .andExpectAll(view().name(membershipProcessPage),
                        model().attributeExists(MEMBER)));
    }
}

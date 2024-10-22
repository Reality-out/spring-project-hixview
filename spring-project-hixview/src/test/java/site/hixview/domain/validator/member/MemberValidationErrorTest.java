package site.hixview.domain.validator.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.dto.MemberDto;
import site.hixview.domain.service.MemberService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.MemberTestUtils;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.MEMBER;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;

@RealControllerAndValidatorContext
class MemberValidationErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("미래의 생일을 사용하는 회원 가입")
    @Test
    void futureBirthdayMembership() throws Exception {
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
    void invalidBirthdayMembership() throws Exception {
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

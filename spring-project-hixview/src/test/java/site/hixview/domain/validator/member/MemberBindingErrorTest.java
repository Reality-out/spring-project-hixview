package site.hixview.domain.validator.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.domain.entity.member.MemberDto;
import site.hixview.domain.service.MemberService;
import site.hixview.support.util.MemberTestUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.EntityName.Member.ID;
import static site.hixview.domain.vo.name.EntityName.Member.MEMBER;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;

@OnlyRealControllerContext
class MemberBindingErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("NotBlank에 대한 회원 가입 유효성 검증")
    @Test
    void validateNotBlankMembership() throws Exception {
        // given & when
        MemberDto memberDtoSpace = createTestMemberDto();
        memberDtoSpace.setId(" ");
        memberDtoSpace.setPassword(" ");
        memberDtoSpace.setName(" ");
        memberDtoSpace.setPhoneNumber(" ");
        MemberDto memberDtoNull = createTestMemberDto();
        memberDtoNull.setId(null);
        memberDtoNull.setPassword(null);
        memberDtoNull.setName(null);
        memberDtoNull.setPhoneNumber(null);

        // then
        for (MemberDto memberDto : List.of(memberDtoSpace, memberDtoNull)) {
            assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDtoSpace))
                    .andExpectAll(view().name(membershipProcessPage))
                    .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                    .usingRecursiveComparison()
                    .isEqualTo(memberDtoSpace);
        }
    }

    @DisplayName("NotNull에 대한 회원 가입 유효성 검증")
    @Test
    void validateNotNullMembership() throws Exception {
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
    void validatePatternMembership() throws Exception {
        // given & when
        MemberDto memberDto = createTestMemberDto();
        memberDto.setId(INVALID_VALUE);
        memberDto.setPassword(INVALID_VALUE);
        memberDto.setName(INVALID_VALUE);
        memberDto.setPhoneNumber(INVALID_VALUE);

        // then
        assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                .andExpectAll(view().name(membershipProcessPage))
                .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                .usingRecursiveComparison()
                .isEqualTo(memberDto);
    }

    @DisplayName("Range에 대한 회원 가입 유효성 검증")
    @Test
    void validateRangeMembership() throws Exception {
        // given & when
        MemberDto memberDtoFallShortOf = createTestMemberDto();
        memberDtoFallShortOf.setMonth(0);
        memberDtoFallShortOf.setDays(0);

        MemberDto memberDtoExceed = createTestMemberDto();
        memberDtoExceed.setYear(2100);
        memberDtoExceed.setMonth(13);
        memberDtoExceed.setDays(32);

        // then
        for (MemberDto memberDto : List.of(memberDtoFallShortOf, memberDtoExceed)) {
            assertThat(requireNonNull(mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, memberDto))
                    .andExpectAll(view().name(membershipProcessPage))
                    .andReturn().getModelAndView()).getModelMap().get(MEMBER))
                    .usingRecursiveComparison()
                    .isEqualTo(memberDto);
        }
    }

    @DisplayName("typeMismatch에 대한 회원 가입 유효성 검증")
    @Test
    void validateTypeMismatchMembership() throws Exception {
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

package site.hixview.domain.validator.article.mock;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;
import site.hixview.support.context.RealControllerAndValidatorContext;
import site.hixview.support.util.MemberTestUtils;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.LOGIN_PATH;
import static site.hixview.domain.vo.user.RequestPath.MEMBERSHIP_PATH;
import static site.hixview.util.ControllerUtils.decodeWithUTF8;

@RealControllerAndValidatorContext
public class UserMemberValidationErrorTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("중복 ID를 사용하는 회원 가입")
    @Test
    void duplicatedIdMembership() throws Exception {
        // given
        Member member = testMember;
        String id = member.getId();
        MembershipDto membershipDtoId = createTestMembershipDto();
        membershipDtoId.setId(id);
        when(memberService.registerMember(member)).thenReturn(member);
        when(memberService.findMemberByID(id)).thenReturn(Optional.of(member));
        when(memberService.findMemberByEmail(member.getEmail())).thenReturn(Optional.empty());

        // when
        memberService.registerMember(member);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMembershipDto(MEMBERSHIP_PATH, membershipDtoId))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(BASIC_LAYOUT);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(ID)))
                .isEqualTo("이미 가입된 ID입니다.");
    }

    @DisplayName("중복 이메일을 사용하는 회원 가입")
    @Test
    void duplicatedEmailMembership() throws Exception {
        // given
        Member member = testMember;
        String email = member.getEmail();
        MembershipDto membershipDtoId = createTestMembershipDto();
        membershipDtoId.setEmail(email);
        when(memberService.registerMember(member)).thenReturn(member);
        when(memberService.findMemberByID(member.getId())).thenReturn(Optional.empty());
        when(memberService.findMemberByEmail(email)).thenReturn(Optional.of(member));

        // when
        memberService.registerMember(member);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithMembershipDto(MEMBERSHIP_PATH, membershipDtoId))
                .andExpectAll(status().isBadRequest()).andReturn().getResponse().getContentAsString(), new TypeReference<>(){});
        assertThat(jsonMap.get(LAYOUT_PATH)).isEqualTo(BASIC_LAYOUT);

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8((fieldErrorMap).get(EMAIL)))
                .isEqualTo("이미 가입된 이메일입니다.");
    }
    
    @DisplayName("존재하지 않는 ID를 사용하는 로그인")
    @Test
    void notFoundIDLogin() throws Exception {
        // given & when
        LoginDto loginDto = new LoginDto();
        loginDto.setId(INVALID_VALUE);
        loginDto.setPassword(INVALID_VALUE);
        String id = loginDto.getId();
        when(memberService.findMemberByID(id)).thenReturn(Optional.empty());
        when(memberService.findMemberByIDAndPassword(id, loginDto.getPassword())).thenReturn(Optional.empty());

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(ID))).isEqualTo("해당 ID가 존재하지 않습니다.");
    }

    @DisplayName("존재하지 않는 회원 정보를 사용하는 로그인")
    @Test
    void notFoundMemberInfoLogin() throws Exception {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setId(testMember.getId());
        loginDto.setPassword(INVALID_VALUE);
        String id = loginDto.getId();
        when(memberService.findMemberByID(id)).thenReturn(Optional.of(testMember));
        when(memberService.findMemberByIDAndPassword(id, loginDto.getPassword())).thenReturn(Optional.empty());
        when(memberService.registerMember(testMember)).thenReturn(testMember);

        // when
        memberService.registerMember(testMember);

        // then
        Map<String, Object> jsonMap = new ObjectMapper().readValue(mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isBadRequest(),
                        jsonPath(LAYOUT_PATH).value(BASIC_LAYOUT))
                .andReturn().getResponse().getContentAsString(), new TypeReference<>() {
        });

        @SuppressWarnings("unchecked")
        Map<String, String> fieldErrorMap = (Map<String, String>) jsonMap.get(FIELD_ERROR_MAP);
        assertThat(decodeWithUTF8(fieldErrorMap.get(PASSWORD))).isEqualTo("해당 정보에 맞는 회원이 존재하지 않습니다.");
    }
}

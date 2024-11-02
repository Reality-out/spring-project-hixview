package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.domain.entity.member.dto.MembershipDto;
import site.hixview.domain.service.MemberService;
import site.hixview.support.context.OnlyRealControllerContext;
import site.hixview.support.util.MemberTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;
import static site.hixview.domain.vo.RequestPath.FINISH_PATH;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_FINISH;
import static site.hixview.domain.vo.name.ViewName.VIEW_SHOW;
import static site.hixview.domain.vo.user.Layout.BASIC_LAYOUT;
import static site.hixview.domain.vo.user.RequestPath.*;
import static site.hixview.domain.vo.user.RequestPath.LOGIN_PATH;
import static site.hixview.domain.vo.user.ViewName.LOGIN_VIEW;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;
import static site.hixview.util.ControllerUtils.encodeWithUTF8;

@OnlyRealControllerContext
class UserMemberControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("회원 가입 페이지 접속")
    @Test
    void accessMembership() throws Exception {
        mockMvc.perform(get(MEMBERSHIP_PATH))
                .andExpectAll(status().isOk(),
                        view().name(membershipProcessPage),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("회원 가입 완료 페이지 접속")
    @Test
    void accessMembershipFinish() throws Exception {
        // given
        MembershipDto membershipDto = createTestMembershipDto();
        Member member = Member.builder().membershipDto(membershipDto).build();
        String name = membershipDto.getName();
        String redirectPath = MEMBERSHIP_PATH + FINISH_PATH;

        // when
        when(memberService.registerMember(member)).thenReturn(member);

        // then
        mockMvc.perform(postWithMembershipDto(MEMBERSHIP_PATH, membershipDto))
                .andExpectAll(status().isSeeOther(),
                        header().string(HttpHeaders.LOCATION, redirectPath),
                        jsonPath(NAME).value(name),
                        jsonPath(REDIRECT_PATH).value(redirectPath));

        mockMvc.perform(get(fromPath(redirectPath).queryParam(NAME, encodeWithUTF8(name)).build().toUriString()))
                .andExpectAll(status().isOk(),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT),
                        view().name(MEMBERSHIP_VIEW + VIEW_FINISH));
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    void accessLogin() throws Exception {
        mockMvc.perform(get(LOGIN_PATH))
                .andExpectAll(status().isOk(),
                        view().name(LOGIN_VIEW + VIEW_SHOW),
                        model().attribute("membership", MEMBERSHIP_PATH),
                        model().attribute("findId", FIND_ID_PATH));
    }

    @DisplayName("로그인 완료 페이지 접속")
    @Test
    void accessLoginFinish() throws Exception {
        // given
        LoginDto loginDto = new LoginDto();
        loginDto.setId(testMember.getId());
        loginDto.setPassword(testMember.getPassword());
        when(memberService.findMemberByID(loginDto.getId())).thenReturn(Optional.of(testMember));
        when(memberService.findMemberByIDAndPassword(loginDto.getId(), loginDto.getPassword())).thenReturn(Optional.of(testMember));
        when(memberService.registerMember(testMember)).thenReturn(testMember);

        // when
        memberService.registerMember(testMember);

        // then
        mockMvc.perform(postWithLoginDto(LOGIN_PATH, loginDto))
                .andExpectAll(status().isSeeOther(),
                        header().string(HttpHeaders.LOCATION, ""),
                        jsonPath(REDIRECT_PATH).value(""));
    }
}
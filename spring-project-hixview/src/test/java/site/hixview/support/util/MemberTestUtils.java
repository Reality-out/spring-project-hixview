package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.LoginDto;
import site.hixview.domain.entity.member.dto.LoginInfoDto;
import site.hixview.domain.entity.member.dto.MemberDto;
import site.hixview.domain.entity.member.dto.MembershipDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;

public interface MemberTestUtils extends ObjectTestUtils {
    // Assertion
    String membershipProcessPage = MEMBERSHIP_VIEW + VIEW_PROCESS;

    // Schema Name
    String TEST_MEMBERS_SCHEMA = "test_members";

    // Test Object
    Member testMember = Member.builder().id("ABcd1234!").password("EFgh1234!").name("박진하")
            .email("abcd1234@naver.com").build();

    Member testNewMember = Member.builder().id("abCD4321!").password("OPqr4321!").name("박하진")
            .email("efgh5678@gmail.com").build();

    default MemberDto createTestMemberDto() {
        return testMember.toDto();
    }

    default MemberDto createTestNewMemberDto() {
        return testNewMember.toDto();
    }

    default MembershipDto createTestMembershipDto() {
        MembershipDto membershipDto = new MembershipDto();
        membershipDto.setId(testMember.getId());
        membershipDto.setPassword(testMember.getPassword());
        membershipDto.setName(testMember.getName());
        membershipDto.setEmail(testMember.getEmail());
        return membershipDto;
    }

    default LoginDto createTestMemberLoginDto() {
        LoginDto loginDto = new LoginDto();
        loginDto.setId(testMember.getId());
        loginDto.setPassword(testMember.getPassword());
        return loginDto;
    }

    default LoginInfoDto createTestMemberLoginInfoDto() {
        LoginInfoDto loginInfoDto = new LoginInfoDto();
        loginInfoDto.setId(testMember.getId());
        loginInfoDto.setName(testMember.getName());
        return loginInfoDto;
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithMember(String url, Member member) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(IDENTIFIER, member.getIdentifier().toString())
                .param(ID, member.getId())
                .param(PASSWORD, member.getPassword())
                .param(NAME, member.getName())
                .param(EMAIL, member.getEmail());
    }

    default MockHttpServletRequestBuilder postWithMemberDto(String url, MemberDto memberDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, memberDto.getId())
                .param(PASSWORD, memberDto.getPassword())
                .param(NAME, memberDto.getName())
                .param(EMAIL, memberDto.getEmail());
    }

    default MockHttpServletRequestBuilder postWithMembershipDto(String url, MembershipDto membershipDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, membershipDto.getId())
                .param(PASSWORD, membershipDto.getPassword())
                .param(NAME, membershipDto.getName())
                .param(EMAIL, membershipDto.getEmail());
    }

    default MockHttpServletRequestBuilder postWithLoginDto(String url, LoginDto loginDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, loginDto.getId())
                .param(PASSWORD, loginDto.getPassword());
    }
}

package site.hixview.support.util;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.entity.member.dto.MemberDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static site.hixview.domain.vo.Word.*;
import static site.hixview.domain.vo.name.EntityName.Member.ID;
import static site.hixview.domain.vo.name.ViewName.VIEW_PROCESS;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;

public interface MemberTestUtils extends ObjectTestUtils {
    // Assertion
    String membershipProcessPage = MEMBERSHIP_VIEW + VIEW_PROCESS;

    // Schema Name
    String TEST_MEMBERS_SCHEMA = "test_members";

    // Test Object
    Member testMember = Member.builder().id("ABcd1234!").password("EFgh1234!").name("박진하")
            .birthday(LocalDate.of(2000, 4, 1)).phoneNumber("010-1234-5678").build();

    Member testNewMember = Member.builder().id("abCD4321!").password("OPqr4321!").name("박하진")
            .birthday(LocalDate.of(1999, 9, 1)).phoneNumber("010-2345-6789").build();

    default MemberDto createTestMemberDto() {
        return testMember.toDto();
    }

    default MemberDto createTestNewMemberDto() {
        return testNewMember.toDto();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder postWithMember(String url, Member member) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, member.getId())
                .param("password", member.getPassword())
                .param(NAME, member.getName())
                .param(YEAR, String.valueOf(member.getBirthday().getYear()))
                .param(MONTH, String.valueOf(member.getBirthday().getMonthValue()))
                .param(DAYS, String.valueOf(member.getBirthday().getDayOfMonth()))
                .param("phoneNumber", member.getPhoneNumber().toStringWithDash());
    }

    default MockHttpServletRequestBuilder postWithMemberDto(String url, MemberDto memberDto) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, memberDto.getId())
                .param("password", memberDto.getPassword())
                .param(NAME, memberDto.getName())
                .param(YEAR, String.valueOf(memberDto.getYear()))
                .param(MONTH, String.valueOf(memberDto.getMonth()))
                .param(DAYS, String.valueOf(memberDto.getDays()))
                .param("phoneNumber", memberDto.getPhoneNumber());
    }
}

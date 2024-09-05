package springsideproject1.springsideproject1build.util.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.entity.member.MemberDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.ID;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.MEMBERSHIP_VIEW;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.VIEW_PROCESS_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.*;

public interface MemberTestUtils extends ObjectTestUtils {
    // Assertion
    String membershipProcessPage = MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX;

    // Test Object
    Member testMember = Member.builder().id("ABcd1234!").password("EFgh1234!").name("박진하")
            .birth(LocalDate.of(2000, 4, 1)).phoneNumber("010-1234-5678").build();

    Member testNewMember = Member.builder().id("abCD4321!").password("OPqr4321!").name("박하진")
            .birth(LocalDate.of(1999, 9, 1)).phoneNumber("010-2345-6789").build();

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
                .param(YEAR, String.valueOf(member.getBirth().getYear()))
                .param(MONTH, String.valueOf(member.getBirth().getMonthValue()))
                .param(DAYS, String.valueOf(member.getBirth().getDayOfMonth()))
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

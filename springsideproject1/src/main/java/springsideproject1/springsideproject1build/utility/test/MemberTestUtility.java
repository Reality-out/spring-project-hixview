package springsideproject1.springsideproject1build.utility.test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import springsideproject1.springsideproject1build.domain.member.Member;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;

public interface MemberTestUtility extends ObjectTestUtility {

    // DB table name
    String memberTable = "testmembers";

    /**
     * Create
     */
    default Member createTestMember() {
        return Member.builder().id("ABcd1234!").password("EFgh1234!").name("박진하")
                .birth(LocalDate.of(2000, 4, 1)).phoneNumber("010-1234-5678").build();
    }

    default Member createTestNewMember() {
        return Member.builder().id("abCD4321!").password("OPqr4321!").name("박하진")
                .birth(LocalDate.of(1999, 9, 1)).phoneNumber("010-2345-6789").build();
    }

    /**
     * Request
     */
    default MockHttpServletRequestBuilder processPostWithMember(String url, Member member) {
        return post(url).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(ID, member.getId())
                .param("password", member.getPassword())
                .param(NAME, member.getName())
                .param("year", String.valueOf(member.getBirth().getYear()))
                .param("month", String.valueOf(member.getBirth().getMonthValue()))
                .param(DATE, String.valueOf(member.getBirth().getDayOfMonth()))
                .param("phoneNumber", member.getPhoneNumber().toString());
    }
}

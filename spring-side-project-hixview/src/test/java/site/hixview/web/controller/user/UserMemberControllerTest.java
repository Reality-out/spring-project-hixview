package site.hixview.web.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.util.test.MemberTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.name.EntityName.Member.MEMBER;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.ViewName.FINISH_VIEW;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMemberControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원 가입 페이지 접속")
    @Test
    public void accessMembership() throws Exception {
        mockMvc.perform(get(MEMBERSHIP_URL))
                .andExpectAll(status().isOk(),
                        view().name(membershipProcessPage),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("회원 가입 완료 페이지 접속")
    @Test
    public void accessMembershipFinish() throws Exception {
        mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, createTestMemberDto()))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrl(MEMBERSHIP_URL + FINISH_URL));

        mockMvc.perform(get(MEMBERSHIP_URL + FINISH_URL))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + FINISH_VIEW));
    }
}
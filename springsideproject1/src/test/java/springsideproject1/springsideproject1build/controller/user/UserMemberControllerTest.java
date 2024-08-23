package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.MEMBERSHIP_URL;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMemberControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입 페이지 접속")
    @Test
    public void accessMembership() throws Exception {
        mockMvc.perform(get(MEMBERSHIP_URL))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("회원가입 완료 페이지 접속")
    @Test
    public void accessMembershipFinish() throws Exception {
        mockMvc.perform(postWithMember(MEMBERSHIP_URL, testMember))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrl(MEMBERSHIP_URL + URL_FINISH_SUFFIX));

        mockMvc.perform(get(MEMBERSHIP_URL + URL_FINISH_SUFFIX))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX));
    }
}
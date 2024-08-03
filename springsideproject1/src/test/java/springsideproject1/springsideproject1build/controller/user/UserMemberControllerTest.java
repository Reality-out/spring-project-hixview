package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.utility.test.MemberTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMemberControllerTest implements MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("회원가입 페이지 접속")
    @Test
    public void accessMembershipPage() throws Exception {
        mockMvc.perform(get("/membership"))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + VIEW_PROCESS_SUFFIX));
    }

    @DisplayName("회원가입 완료 페이지 접속")
    @Test
    public void accessMembershipSucceedPage() throws Exception {
        mockMvc.perform(processPostWithMember("/membership", createTestMember()))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrl("/membership" + URL_FINISH_SUFFIX));

        mockMvc.perform(get("/membership" + URL_FINISH_SUFFIX))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + VIEW_FINISH_SUFFIX));
    }
}
package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static springsideproject1.springsideproject1build.Utility.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public UserMemberControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    public void accessLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/loginPage"));
    }

    @DisplayName("회원가입 페이지 접속")
    @Test
    public void accessMembershipPage() throws Exception {
        mockMvc.perform(get("/membership"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/membership/createPage"));
    }

    @DisplayName("회원가입 완료 페이지 접속")
    @Test
    public void accessMembershipSucceedPage() throws Exception {
        Member member = createTestMember();

        mockMvc.perform(post("/membership")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", member.getId())
                        .param("password", member.getPassword())
                        .param("name", member.getName()))
                .andExpect(status().isSeeOther());

        mockMvc.perform(get("/membership/succeed"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/membership/succeedPage"));
    }
}
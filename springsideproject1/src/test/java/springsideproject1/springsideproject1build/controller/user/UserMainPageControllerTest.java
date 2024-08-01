package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.FOLDER_PATH_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.USER_LOGIN_VIEW_NAME;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMainPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public UserMainPageControllerTest(DataSource dataSource) {
        JdbcTemplate jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @DisplayName("메인 페이지 접속")
    @Test
    public void accessMainPage() throws Exception {
        mockMvc.perform(get(""))
                .andExpect(status().isOk())
                .andExpect(view().name("user/mainPage"))
                .andExpect(model().attribute("layoutPath", BASIC_LAYOUT_PATH));
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    public void accessLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_LOGIN_VIEW_NAME + "loginPage"));
    }
}
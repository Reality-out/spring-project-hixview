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
import springsideproject1.springsideproject1build.service.MemberService;
import springsideproject1.springsideproject1build.utility.test.MemberTest;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.FIND_ID_URL;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.MainUtility.toStringForUrl;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMainControllerTest implements MemberTest {

    @Autowired private MockMvc mockMvc;
    @Autowired MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public UserMainControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
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
                .andExpect(view().name(USER_LOGIN_VIEW + "loginPage"))
                .andExpect(model().attribute("findId", FIND_ID_URL));
    }

    @DisplayName("아이디 찾기 페이지 접속")
    @Test
    public void accessFindIdPage() throws Exception {
        mockMvc.perform(get(FIND_ID_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_FIND_ID_VIEW + VIEW_PROCESS_SUFFIX));
    }

    @DisplayName("아이디 찾기 완료 페이지 접속")
    @Test
    public void accessFindIdFinishPage() throws Exception {
        // given
        Member member1 = createTestMember();
        String commonName = member1.getName();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(createTestNewMember()).name(commonName).birth(commonBirth).build();

        // when
        memberService.joinMember(member1);
        memberService.joinMember(member2);

        // then
        List<String> idList = List.of(member1.getId(), member2.getId());
        String idListForUrl = toStringForUrl(idList);
        assertThat(mockMvc.perform(post(FIND_ID_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", commonName)
                        .param("year", String.valueOf(commonBirth.getYear()))
                        .param("month", String.valueOf(commonBirth.getMonthValue()))
                        .param("date", String.valueOf(commonBirth.getDayOfMonth()))
                )
                .andExpect(status().isSeeOther())
                .andExpect(redirectedUrlPattern(FIND_ID_URL + URL_FINISH_SUFFIX + "?*"))
                .andReturn().getModelAndView().getModelMap().get("idList"))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);

        mockMvc.perform(get(FIND_ID_URL + URL_FINISH_SUFFIX)
                        .param("idList", idListForUrl))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_FIND_ID_VIEW + VIEW_FINISH_SUFFIX))
                .andExpect(model().attribute("idList", idList));
    }
}
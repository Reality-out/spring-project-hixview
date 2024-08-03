package springsideproject1.springsideproject1build.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.Member;
import springsideproject1.springsideproject1build.service.MemberService;
import springsideproject1.springsideproject1build.utility.test.MemberTest;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtility.*;
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
                .andExpectAll(status().isOk(),
                        view().name("user/mainPage"),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH));
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    public void accessLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpectAll(status().isOk(),
                        view().name(USER_LOGIN_VIEW + "loginPage"),
                        model().attribute("membership", MEMBERSHIP_URL),
                        model().attribute("findId", FIND_ID_URL));
    }

    @DisplayName("아이디 찾기 페이지 접속")
    @Test
    public void accessFindIdPage() throws Exception {
        mockMvc.perform(get(FIND_ID_URL))
                .andExpectAll(status().isOk(),
                        view().name(USER_FIND_ID_VIEW + VIEW_PROCESS_SUFFIX));
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
        String idListString = "idList";

        assertThat(mockMvc.perform(processPostWithMultipleParam(FIND_ID_URL, new HashMap<>(){{
            put(NAME, commonName);
            put(YEAR, String.valueOf(commonBirth.getYear()));
            put(MONTH, String.valueOf(commonBirth.getMonthValue()));
            put(DATE, String.valueOf(commonBirth.getDayOfMonth()));
        }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(FIND_ID_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView().getModelMap().get(idListString))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);

        mockMvc.perform(processGetWithSingleParam(FIND_ID_URL + URL_FINISH_SUFFIX, idListString, idListForUrl))
                .andExpectAll(status().isOk(),
                        view().name(USER_FIND_ID_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(idListString, idList));
    }
}
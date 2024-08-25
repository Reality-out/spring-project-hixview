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
import springsideproject1.springsideproject1build.domain.entity.member.Member;
import springsideproject1.springsideproject1build.domain.service.MemberService;
import springsideproject1.springsideproject1build.util.test.MemberTestUtils;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.DATE;
import static springsideproject1.springsideproject1build.domain.valueobject.CLASS.MEMBER;
import static springsideproject1.springsideproject1build.domain.valueobject.DATABASE.MEMBER_TABLE;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.BASIC_LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.LAYOUT.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.valueobject.REQUEST_URL.*;
import static springsideproject1.springsideproject1build.domain.valueobject.VIEW_NAME.*;
import static springsideproject1.springsideproject1build.domain.valueobject.WORD.NAME;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserMainControllerTest implements MemberTestUtils {

    @Autowired private MockMvc mockMvc;
    @Autowired MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public UserMainControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, MEMBER_TABLE, true);
    }

    @DisplayName("쿼리 문자열에서의 특수문자 처리 방식 확인")
    @Test
    public void checkSpecialCharacterLogicInQueryString() throws Exception {
        // given
        Member member1 = Member.builder().member(testMember).id("a1!@#$%^&*()-_+=").build();
        String commonName = member1.getName();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(testNewMember).id("b2{[}]\\|;:'\"<,>.?/").name(commonName).birth(commonBirth).build();

        // when
        memberService.registerMember(member1);
        memberService.registerMember(member2);

        // then
        List<String> idList = List.of(member1.getId(), member2.getId());
        String idListForUrl = toStringForUrl(idList);
        String idListString = "idList";

        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(FIND_ID_URL, new HashMap<>() {{
                    put(NAME, commonName);
                    put("year", String.valueOf(commonBirth.getYear()));
                    put("month", String.valueOf(commonBirth.getMonthValue()));
                    put(DATE, String.valueOf(commonBirth.getDayOfMonth()));
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(FIND_ID_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap().get(idListString))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);
    }

    @DisplayName("유저 메인 페이지 접속")
    @Test
    public void accessUserMainPage() throws Exception {
        mockMvc.perform(get(""))
                .andExpectAll(status().isOk(),
                        view().name("user/mainPage"),
                        model().attribute(LAYOUT_PATH, BASIC_LAYOUT_PATH));
    }

    @DisplayName("로그인 페이지 접속")
    @Test
    public void accessLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpectAll(status().isOk(),
                        view().name(USER_LOGIN_VIEW + "loginPage"),
                        model().attribute("membership", MEMBERSHIP_URL),
                        model().attribute("findId", FIND_ID_URL));
    }

    @DisplayName("아이디 찾기 페이지 접속")
    @Test
    public void accessFindId() throws Exception {
        mockMvc.perform(get(FIND_ID_URL))
                .andExpectAll(status().isOk(),
                        view().name(USER_FIND_ID_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("아이디 찾기 완료 페이지 접속")
    @Test
    public void accessFindIdFinish() throws Exception {
        // given
        Member member1 = testMember;
        String commonName = member1.getName();
        LocalDate commonBirth = member1.getBirth();
        Member member2 = Member.builder().member(testNewMember).name(commonName).birth(commonBirth).build();

        // when
        memberService.registerMember(member1);
        memberService.registerMember(member2);

        // then
        List<String> idList = List.of(member1.getId(), member2.getId());
        String idListForUrl = toStringForUrl(idList);
        String idListString = "idList";

        assertThat(requireNonNull(mockMvc.perform(postWithMultipleParams(FIND_ID_URL, new HashMap<>() {{
                    put(NAME, commonName);
                    put("year", String.valueOf(commonBirth.getYear()));
                    put("month", String.valueOf(commonBirth.getMonthValue()));
                    put(DATE, String.valueOf(commonBirth.getDayOfMonth()));
                }}))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(FIND_ID_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING))
                .andReturn().getModelAndView()).getModelMap().get(idListString))
                .usingRecursiveComparison()
                .isEqualTo(idListForUrl);

        mockMvc.perform(getWithSingleParam(FIND_ID_URL + URL_FINISH_SUFFIX, idListString, idListForUrl))
                .andExpectAll(status().isOk(),
                        view().name(USER_FIND_ID_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(idListString, idList));
    }
}
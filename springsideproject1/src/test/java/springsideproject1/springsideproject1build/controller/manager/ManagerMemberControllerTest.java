package springsideproject1.springsideproject1build.controller.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import springsideproject1.springsideproject1build.domain.member.Member;
import springsideproject1.springsideproject1build.service.MemberService;
import springsideproject1.springsideproject1build.utility.test.MemberTestUtility;

import javax.sql.DataSource;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.config.constant.LAYOUT_CONFIG.SELECT_PATH;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.*;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;
import static springsideproject1.springsideproject1build.utility.ConstantUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerMemberControllerTest implements MemberTestUtility {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberService memberService;

    private final JdbcTemplate jdbcTemplateTest;

    @Autowired
    public ManagerMemberControllerTest(DataSource dataSource) {
        jdbcTemplateTest = new JdbcTemplate(dataSource);
    }

    @BeforeEach
    public void beforeEach() {
        resetTable(jdbcTemplateTest, memberTable, true);
    }

    @DisplayName("멤버들 보기 페이지 접속")
    @Test
    public void accessMembersSee() throws Exception {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        member1 = memberService.registerMember(member1);
        member2 = memberService.registerMember(member2);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_MEMBER_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_SELECT_VIEW + "membersPage"),
                        model().attribute(LAYOUT_PATH, SELECT_PATH))
                .andReturn().getModelAndView()).getModelMap().get("members"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 탈퇴 페이지 접속")
    @Test
    public void accessMembershipWithdraw() throws Exception {
        mockMvc.perform(get(REMOVE_MEMBER_URL))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_PROCESS_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, "회원"),
                        model().attribute(DATA_TYPE_ENGLISH, MEMBER),
                        model().attribute(REMOVE_KEY, ID));
    }

    @DisplayName("회원 탈퇴 완료 페이지 접속")
    @Test
    public void accessMembershipWithdrawFinish() throws Exception {
        // given
        Member member = createTestMember();

        // when
        memberService.registerMember(member);

        // then
        String id = member.getId();

        mockMvc.perform(postWithSingleParam(REMOVE_MEMBER_URL, ID, id))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrlPattern(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX + ALL_QUERY_STRING),
                        model().attribute(ID, id));

        mockMvc.perform(getWithSingleParam(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX, ID, id))
                .andExpectAll(status().isOk(),
                        view().name(MANAGER_REMOVE_VIEW + VIEW_FINISH_SUFFIX),
                        model().attribute(DATA_TYPE_KOREAN, "회원"),
                        model().attribute(KEY, ID),
                        model().attribute(VALUE, id));
    }
}
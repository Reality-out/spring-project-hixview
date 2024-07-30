package springsideproject1.springsideproject1build.controller.manager;

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

import javax.sql.DataSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.Utility.*;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.REMOVE_MEMBER_URL;
import static springsideproject1.springsideproject1build.config.constant.REQUEST_URL_CONFIG.URL_FINISH_SUFFIX;
import static springsideproject1.springsideproject1build.config.constant.VIEW_NAME_CONFIG.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerMemberControllerTest {

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

    @DisplayName("멤버 리스트 페이지 접속")
    @Test
    public void accessMembersPage() throws Exception {
        // given
        Member member1 = createTestMember();
        Member member2 = createTestNewMember();

        // when
        member1 = memberService.joinMember(member1);
        member2 = memberService.joinMember(member2);

        // then
        assertThat(mockMvc.perform(get("/manager/member/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("manager/select/membersPage"))
                .andReturn().getModelAndView().getModelMap().get("members"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(member1, member2));
    }

    @DisplayName("회원 탈퇴 페이지 접속")
    @Test
    public void accessMembershipWithdrawPage() throws Exception {
        mockMvc.perform(get(REMOVE_MEMBER_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(MANAGER_REMOVE_VIEW_NAME + VIEW_NAME_PROCESS_SUFFIX))
                .andExpect(model().attribute("dataTypeKor", "회원"))
                .andExpect(model().attribute("dataTypeEng", "member"))
                .andExpect(model().attribute("key", "id"));
    }

    @DisplayName("회원 탈퇴 완료 페이지 접속")
    @Test
    public void accessMembershipWithdrawFinishPage() throws Exception {
        // given
        Member member = createTestMember();

        // when
        memberService.joinMember(member);

        // then
        String id = member.getId();

        mockMvc.perform(post(REMOVE_MEMBER_URL)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", id))
                .andExpect(status().isSeeOther())
                .andExpect(redirectedUrlPattern(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX + "?*"))
                .andExpect(model().attribute("id", id));

        mockMvc.perform(get(REMOVE_MEMBER_URL + URL_FINISH_SUFFIX)
                        .param("id", id))
                .andExpect(status().isOk())
                .andExpect(view().name(MANAGER_REMOVE_VIEW_NAME + VIEW_NAME_FINISH_SUFFIX))
                .andExpect(model().attribute("dataTypeKor", "회원"))
                .andExpect(model().attribute("key", "id"))
                .andExpect(model().attribute("value", id));
    }
}
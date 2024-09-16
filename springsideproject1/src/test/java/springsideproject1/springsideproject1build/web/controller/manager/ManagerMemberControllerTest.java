package springsideproject1.springsideproject1build.web.controller.manager;

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
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springsideproject1.springsideproject1build.domain.vo.SchemaName.TEST_MEMBERS_SCHEMA;
import static springsideproject1.springsideproject1build.domain.vo.Word.LAYOUT_PATH;
import static springsideproject1.springsideproject1build.domain.vo.manager.Layout.SELECT_LAYOUT;
import static springsideproject1.springsideproject1build.domain.vo.manager.RequestUrl.SELECT_MEMBER_URL;
import static springsideproject1.springsideproject1build.domain.vo.manager.ViewName.SELECT_VIEW;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ManagerMemberControllerTest implements MemberTestUtils {

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
        resetTable(jdbcTemplateTest, TEST_MEMBERS_SCHEMA, true);
    }

    @DisplayName("회원들 조회 페이지 접속")
    @Test
    public void accessMembersInquiry() throws Exception {
        // given
        Member member1 = testMember;
        Member member2 = testNewMember;

        // when
        member1 = memberService.registerMember(member1);
        member2 = memberService.registerMember(member2);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_MEMBER_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "members-page"),
                        model().attribute(LAYOUT_PATH, SELECT_LAYOUT))
                .andReturn().getModelAndView()).getModelMap().get("members"))
                .usingRecursiveComparison()
                .isEqualTo(List.of(member1, member2));
    }
}
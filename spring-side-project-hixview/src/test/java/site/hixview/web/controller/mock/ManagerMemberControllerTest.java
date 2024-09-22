package site.hixview.web.controller.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.postprocessor.MockServiceBeanFactoryPostProcessor;
import site.hixview.domain.postprocessor.MockValidatorBeanFactoryPostProcessor;
import site.hixview.domain.service.MemberService;
import site.hixview.util.test.MemberTestUtils;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.Word.LAYOUT_PATH;
import static site.hixview.domain.vo.manager.Layout.SELECT_LAYOUT;
import static site.hixview.domain.vo.manager.RequestURL.SELECT_MEMBER_URL;
import static site.hixview.domain.vo.manager.ViewName.SELECT_VIEW;

@WebMvcTest(properties = {"junit.jupiter.execution.parallel.mode.classes.default=concurrent"})
@Import({MockServiceBeanFactoryPostProcessor.class,
        MockValidatorBeanFactoryPostProcessor.class})
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class ManagerMemberControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @DisplayName("회원들 조회 페이지 접속")
    @Test
    void accessMembersInquiry() throws Exception {
        // given
        Member member1 = Member.builder().member(testMember).identifier(1L).build();
        Member member2 = Member.builder().member(testNewMember).identifier(2L).build();
        List<Member> storedList = List.of(member1, member2);
        when(memberService.findMembers()).thenReturn(storedList);
        when(memberService.registerMember(testMember)).thenReturn(member1);
        when(memberService.registerMember(testNewMember)).thenReturn(member2);

        // when
        memberService.registerMember(testMember);
        memberService.registerMember(testNewMember);

        // then
        assertThat(requireNonNull(mockMvc.perform(get(SELECT_MEMBER_URL))
                .andExpectAll(status().isOk(),
                        view().name(SELECT_VIEW + "members-page"),
                        model().attribute(LAYOUT_PATH, SELECT_LAYOUT))
                .andReturn().getModelAndView()).getModelMap().get("members"))
                .usingRecursiveComparison()
                .isEqualTo(storedList);
    }
}
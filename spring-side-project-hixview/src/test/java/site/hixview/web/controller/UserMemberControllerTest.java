package site.hixview.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import site.hixview.domain.entity.member.Member;
import site.hixview.domain.service.MemberService;
import site.hixview.domain.validation.validator.MemberBirthValidator;
import site.hixview.util.test.MemberTestUtils;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static site.hixview.domain.vo.name.EntityName.Member.MEMBER;
import static site.hixview.domain.vo.RequestUrl.FINISH_URL;
import static site.hixview.domain.vo.name.ViewName.VIEW_FINISH;
import static site.hixview.domain.vo.user.RequestUrl.MEMBERSHIP_URL;
import static site.hixview.domain.vo.user.ViewName.MEMBERSHIP_VIEW;

@SpringBootTest(properties = {"junit.jupiter.execution.parallel.mode.classes.default=concurrent"})
@AutoConfigureMockMvc
@Transactional
@ExtendWith(MockitoExtension.class)
@Execution(ExecutionMode.CONCURRENT)
class UserMemberControllerTest implements MemberTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserMemberController userMemberController;

    @MockBean
    private MemberService memberService;

    @Mock
    private MemberBirthValidator memberBirthValidator;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }
    
    @DisplayName("회원 가입 페이지 접속")
    @Test
    void accessMembership() throws Exception {
        mockMvc.perform(get(MEMBERSHIP_URL))
                .andExpectAll(status().isOk(),
                        view().name(membershipProcessPage),
                        model().attributeExists(MEMBER));
    }

    @DisplayName("회원 가입 완료 페이지 접속")
    @Test
    void accessMembershipFinish() throws Exception {
        // given & when
        Member member = testMember;
        when(memberService.registerMember(member)).thenReturn(member);

        // then
        mockMvc.perform(postWithMemberDto(MEMBERSHIP_URL, member.toDto()))
                .andExpectAll(status().isSeeOther(),
                        redirectedUrl(MEMBERSHIP_URL + FINISH_URL));

        mockMvc.perform(get(MEMBERSHIP_URL + FINISH_URL))
                .andExpectAll(status().isOk(),
                        view().name(MEMBERSHIP_VIEW + VIEW_FINISH));
    }
}